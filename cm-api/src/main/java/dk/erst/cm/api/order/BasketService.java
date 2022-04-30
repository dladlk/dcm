package dk.erst.cm.api.order;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.data.Basket;
import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.data.OrderStatus;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.item.CatalogService;
import dk.erst.cm.api.item.ProductService;
import dk.erst.cm.api.order.OrderProducerService.OrderDefaultConfig;
import dk.erst.cm.api.order.OrderProducerService.PartyInfo;
import dk.erst.cm.api.order.data.CustomerOrderData;
import dk.erst.cm.xml.ubl21.model.Party;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;

@Service
@Slf4j
public class BasketService {

	private final ProductService productService;
	private final OrderService orderService;
	private final OrderProducerService orderProducerService;
	private final CatalogService catalogService;

	@Autowired
	public BasketService(ProductService productService, OrderService orderService, CatalogService catalogService, OrderProducerService orderProducerService) {
		this.productService = productService;
		this.orderService = orderService;
		this.catalogService = catalogService;
		this.orderProducerService = orderProducerService;
	}

	@Data
	public static class SentBasketData {
		private Basket basket;
		private List<Order> orderList;
	}

	@Data
	public static class BasketData {
		private Map<String, Integer> orderLines;
	}

	@Data
	public static class SendBasketData {
		private BasketData basketData;
		private CustomerOrderData orderData;
	}

	@Data
	public static class SendBasketResponse {
		private boolean success;
		private String basketId;
		private String errorMessage;
		private List<String> errorProductIdList;

		public static SendBasketResponse error(String message) {
			SendBasketResponse r = new SendBasketResponse();
			r.setSuccess(false);
			r.setErrorMessage(message);
			return r;
		}

		public SendBasketResponse withErrorProductIdList(List<String> errorProductIdList) {
			this.setErrorProductIdList(errorProductIdList);
			return this;
		}

		public static SendBasketResponse success(String basketId) {
			SendBasketResponse r = new SendBasketResponse();
			r.setSuccess(true);
			r.setBasketId(basketId);
			return r;
		}
	}

	public SendBasketResponse basketSend(SendBasketData query, String outboxFolder, OrderDefaultConfig orderConfig) {
		Set<String> queryProductIdSet = query.basketData.orderLines.keySet();
		Iterable<Product> products = productService.findAllByIds(queryProductIdSet);

		Set<String> resolvedProductIdSet = new HashSet<>();

		log.debug("1. Load necessary data for XML generation");

		Map<String, List<Product>> byCatalogMap = new HashMap<>();
		for (Product p : products) {
			String productCatalogId = p.getProductCatalogId();
			List<Product> productList = byCatalogMap.computeIfAbsent(productCatalogId, k -> new ArrayList<>());
			productList.add(p);
			resolvedProductIdSet.add(p.getId());
		}

		if (resolvedProductIdSet.size() < queryProductIdSet.size()) {
			int countNotFoundProducts = queryProductIdSet.size() - resolvedProductIdSet.size();
			String errorMessage = countNotFoundProducts + " product" + (countNotFoundProducts > 1 ? "s are" : "is") + " not found, please delete highlighted products to send the basket.";
			Set<String> notResolvedProductIdSet = new HashSet<>(queryProductIdSet);
			notResolvedProductIdSet.removeAll(resolvedProductIdSet);
			return SendBasketResponse.error(errorMessage).withErrorProductIdList(new ArrayList<>(notResolvedProductIdSet));
		}

		Set<String> noSellerCatalogSet = new HashSet<>();
		Map<String, Party> sellerPartyByCatalog = new HashMap<>();
		for (String catalogId : byCatalogMap.keySet()) {
			Party sellerParty = catalogService.loadLastSellerParty(catalogId);
			if (sellerParty != null) {
				sellerPartyByCatalog.put(catalogId, sellerParty);
			} else {
				noSellerCatalogSet.add(catalogId);
			}
		}

		if (!noSellerCatalogSet.isEmpty()) {
			int countProductIncompleteSeller = 0;

			List<String> errorProductIdList = new ArrayList<>();
			for (String catalogId : noSellerCatalogSet) {
				List<Product> list = byCatalogMap.get(catalogId);
				errorProductIdList.addAll(list.stream().map(Product::getId).collect(Collectors.toList()));
				countProductIncompleteSeller += list.size();
			}
			String errorMessage = buildErrorMessageNoSellerInfo(byCatalogMap, noSellerCatalogSet, countProductIncompleteSeller);
			return SendBasketResponse.error(errorMessage).withErrorProductIdList(errorProductIdList);
		}

		log.debug("2. Generate internal models for XML");

		Basket basket = new Basket();
		basket.setCreateTime(Instant.now());
		basket.setId(generateId());
		basket.setLineCount(resolvedProductIdSet.size());
		basket.setOrderCount(byCatalogMap.size());
		basket.setVersion(1);

		List<Order> orderList = new ArrayList<>();
		int orderIndex = 0;
		for (String catalogId : byCatalogMap.keySet()) {
			List<Product> productList = byCatalogMap.get(catalogId);

			Order order = new Order();
			order.setBasketId(basket.getId());
			order.setOrderIndex(orderIndex);
			order.setCreateTime(Instant.now());
			order.setId(generateId());
			order.setLineCount(productList.size());
			order.setStatus(OrderStatus.GENERATED);
			order.setOrderNumber(generateOrderNumber(order));
			order.setSupplierName(extractSupplierName(sellerPartyByCatalog.get(catalogId)));
			order.setVersion(1);

			CustomerOrderData customerOrderData = query.getOrderData();
			PartyInfo buyer = new PartyInfo("5798009882806", "0088", customerOrderData.getBuyerCompany().getRegistrationName());
			PartyInfo seller = new PartyInfo("5798009882783", "0088", "Danish Company");

			OrderType sendOrder = orderProducerService.generateOrder(order, orderConfig, buyer, seller, productList);
			order.setDocument(sendOrder);

			orderList.add(order);

			orderIndex++;
		}

		File tempDirectory = createTempDirectory("delis-cm-" + basket.getId());

		log.debug("3. Save XML files into temporary folder " + tempDirectory);

		Map<String, Order> fileNameToOrderMap = new HashMap<>();
		for (Order order : orderList) {
			try {
				File orderFile = orderService.saveOrderXML(tempDirectory, (OrderType) order.getDocument());
				log.debug(" - Saved order XML to " + orderFile.getAbsolutePath());
				order.setResultFileName(orderFile.getName());
				fileNameToOrderMap.put(orderFile.getName(), order);
			} catch (IOException e) {
				log.error(" - Failed to generate xml by order " + order, e);
				return SendBasketResponse.error("Failed to generate XML for order to " + order.getSupplierName() + ": " + e.getMessage());
			}
		}

		log.debug("4. Save basket and orders to database");

		orderService.saveBasket(basket);
		for (Order order : orderList) {
			orderService.saveOrder(order);
		}

		log.debug("5. Move XML files from temporary folder to destination folder at " + outboxFolder);

		File[] tempFiles = tempDirectory.listFiles();
		Set<File> notMovedFiles = new HashSet<>();
		Set<File> movedFiles = new HashSet<>();
		assert tempFiles != null;
		for (File file : tempFiles) {
			File outFile = new File(outboxFolder, file.getName());
			try {
				Order order = fileNameToOrderMap.get(file.getName());
				String supplierName = order.getSupplierName();
				FileUtils.moveFile(file, outFile);
				movedFiles.add(file);
				log.debug(" - Order " + order.getId() + " to " + supplierName + " successfully moved to " + outFile);
			} catch (IOException e) {
				log.error(" - Failed to move file " + file + " to " + outFile, e);
				notMovedFiles.add(file);
			}
		}

		if (!notMovedFiles.isEmpty()) {
			if (movedFiles.isEmpty()) {
				return SendBasketResponse.error("Failed to move XML files to destination folder, please contact system administrator.");
			} else {
				String notSentOrdersToSuppliers = getSupplierNamesByFileSet(notMovedFiles, fileNameToOrderMap);
				String sentOrdersToSuppliers = getSupplierNamesByFileSet(movedFiles, fileNameToOrderMap);
				String errorMessage = notMovedFiles.size() + " file(s) to supplier(s) " + notSentOrdersToSuppliers + " failed to move to destination folder, please contact system administrator. " + movedFiles.size() + " orders to " + sentOrdersToSuppliers + " were sent.";
				return SendBasketResponse.error(errorMessage);
			}
		}

		log.debug("6. Cleanup temporary folder " + tempDirectory);

		// Delete temporary folder anyway
		if (!FileUtils.deleteQuietly(tempDirectory)) {
			log.error("Failed to delete temporary directory, check that streams are closed: " + tempDirectory);
		}

		log.debug("7. Basket #" + basket.getId() + " is sent successfully");

		return SendBasketResponse.success(basket.getId());
	}

	private String generateId() {
		return UUID.randomUUID().toString();
	}

	private String getSupplierNamesByFileSet(Set<File> files, Map<String, Order> fileNameToOrderMap) {
		StringBuilder sb = new StringBuilder();
		for (File file : files) {
			Order order = fileNameToOrderMap.get(file.getName());
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(order.getSupplierName());
		}
		return sb.toString();
	}

	private String buildErrorMessageNoSellerInfo(Map<String, List<Product>> byCatalog, Set<String> noSellerCatalog, int countProductWithCatalogWithoutSeller) {
		StringBuilder sb = new StringBuilder();
		sb.append("Cannot send basket, as ");
		sb.append(countProductWithCatalogWithoutSeller);
		sb.append(" product");
		if (countProductWithCatalogWithoutSeller > 1) {
			sb.append("s");
		}
		int countNoSellerCatalog = noSellerCatalog.size();
		sb.append(" relate to ");
		if (countNoSellerCatalog == 1) {
			sb.append("a");
		} else {
			sb.append(countNoSellerCatalog);
		}
		sb.append(" catalog");
		if (countNoSellerCatalog > 1) {
			sb.append("s");
		}
		sb.append(" for which there is not enough information about seller to send an order.");
		if (byCatalog.size() > countNoSellerCatalog) {
			sb.append(" Remove highlighted products from the basket to send the rest.");
		}
		return sb.toString();
	}

	private File createTempDirectory(String dirName) {
		File tempDirectory = new File(FileUtils.getTempDirectory(), dirName);
		//noinspection ResultOfMethodCallIgnored
		tempDirectory.mkdirs();
		return tempDirectory;
	}

	private String extractSupplierName(Party party) {
		if (party.getPartyName() != null) {
			return party.getPartyName().getName();
		}
		if (party.getPartyLegalEntity() != null) {
			if (party.getPartyLegalEntity().getRegistrationName() != null) {
				return party.getPartyLegalEntity().getRegistrationName();
			}
		}
		return null;
	}

	private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").withZone(ZoneOffset.UTC);

	private String generateOrderNumber(Order order) {
		String creationTimeFormatted = DATE_TIME_FORMATTER.format(order.getCreateTime());
		return creationTimeFormatted + "-" + (order.getOrderIndex() + 1);
	}

	public Optional<SentBasketData> loadSentBasketData(String basketId) {
		Optional<Basket> optional = this.orderService.findBasketById(basketId);
		if (optional.isPresent()) {
			SentBasketData sbd = new SentBasketData();
			sbd.setBasket(optional.get());
			sbd.setOrderList(this.orderService.findOrdersByBasketId(sbd.getBasket().getId()));
			return Optional.of(sbd);
		}
		return Optional.empty();
	}

	public Optional<String> loadSentOrderAsJSON(String orderId) {
		return this.orderService.findOrderByIdAsJSON(orderId);
	}
}
