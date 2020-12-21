package dk.erst.cm.api.item;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.dao.es.ProductES;
import dk.erst.cm.api.dao.es.ProductESRepository;
import dk.erst.cm.api.dao.mongo.ProductRepository;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	private ProductRepository productRepository;
	private ProductESRepository productESRepository;

	@Autowired
	public ProductService(ProductRepository itemRepository, ProductESRepository productESRepository) {
		this.productRepository = itemRepository;
		this.productESRepository = productESRepository;
	}

	public Product saveCatalogUpdateItem(ProductCatalogUpdate catalog, CatalogueLine line) {
		String lineLogicalId = line.getLogicalId();
		String productCatalogId = catalog.getProductCatalogId();

		String itemLogicalId = productCatalogId + "_" + lineLogicalId;

		boolean deleteAction = line.getActionCode() != null && "Delete".equals(line.getActionCode().getId());

		Product product;
		Optional<Product> optional = productRepository.findById(itemLogicalId);
		if (optional.isPresent()) {
			product = optional.get();
			product.setUpdateTime(Instant.now());
			product.setVersion(product.getVersion() + 1);
		} else {
			product = new Product();
			product.setId(itemLogicalId);
			product.setCreateTime(Instant.now());
			product.setUpdateTime(null);
			product.setVersion(1);
		}
		product.setDocumentVersion(ProductDocumentVersion.PEPPOL_CATALOGUE_3_1);
		product.setProductCatalogId(productCatalogId);
		product.setDocument(line);

		if (deleteAction) {
			if (optional.isPresent()) {
				productRepository.delete(product);
				productESRepository.deleteById(product.getId());
			}
			return null;
		}
		productRepository.save(product);
		productESRepository.save(ProductES.by(product));

		return product;
	}

	public long countItems() {
		return productRepository.count();
	}

	public List<Product> findAll(String searchParam) {
		PageRequest p = PageRequest.of(0, 10);

		Page<ProductES> result;
		if (searchParam != null) {
			result = productESRepository.findByNameOrDescriptionOrCertificatesOrOriginOrStandardNumberOrCategoriesOrKeywords(searchParam, searchParam, searchParam, searchParam, searchParam, searchParam, searchParam, p);
		} else {
			result = productESRepository.findAll(p);
		}

		List<String> idList = result.stream().map(pes -> pes.getId()).collect(Collectors.toList());

		log.info("Found " + idList.size() + " ids in ES");

		Iterable<Product> byId = productRepository.findAllById(idList);

		List<Product> productList = StreamSupport.stream(byId.spliterator(), false).collect(Collectors.toList());

		if (idList.size() != productList.size()) {
			log.warn("Number of loaded products from Mongo is different to number of found ids in ES");
		}

		return productList;
		// return productRepository.findAll();
	}

	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}
}
