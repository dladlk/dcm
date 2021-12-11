package dk.erst.cm.api.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Validator;
import com.helger.ubl21.UBL21Writer;

import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.order.data.CustomerOrderData;
import dk.erst.cm.api.order.data.CustomerOrderData.Company;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.Item;
import dk.erst.cm.xml.ubl21.model.NestedID;
import lombok.extern.slf4j.Slf4j;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.OrderLineType;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;

@Slf4j
class OrderProducerServiceTest {

	@Test
	void read() throws IOException {
		try (InputStream is = new FileInputStream("../cm-resources/examples/order/OrderOnly.xml")) {
			OrderType res = UBL21Reader.order().read(is);
			assertEquals("1005", res.getIDValue());
			assertEquals("Contract0101", res.getContract().get(0).getIDValue());
			List<OrderLineType> orderLine = res.getOrderLine();
			for (int i = 0; i < orderLine.size(); i++) {
				OrderLineType orderLineType = orderLine.get(i);
				assertEquals(String.valueOf(i + 1), orderLineType.getLineItem().getIDValue());
			}
		}
	}

	@Test
	void produce() {
		OrderProducerService service = new OrderProducerService();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<Product> productList = new ArrayList<Product>();
		Product product = new Product();
		CatalogueLine catalogueLine = new CatalogueLine();
		Item item = new Item();
		item.setName("Test line");
		item.setSellersItemIdentification(new NestedID());
		item.getSellersItemIdentification().setId("TESTID");
		catalogueLine.setItem(item);
		product.setDocument(catalogueLine);

		productList.add(product);
		CustomerOrderData customerOrderData = new CustomerOrderData();
		Company buyerCompany = new Company();
		buyerCompany.setRegistrationName("Danish Customer Company");
		customerOrderData.setBuyerCompany(buyerCompany);
		Order dataOrder = new Order();
		dataOrder.setCreateTime(Instant.now());
		OrderType order = service.generateOrder(dataOrder, customerOrderData, productList);
		IErrorList errorList = UBL21Validator.order().validate(order);
		if (errorList.isNotEmpty()) {
			System.out.println("Found " + errorList.size() + " errors:");
			for (int i = 0; i < errorList.size(); i++) {
				IError error = errorList.get(i);
				System.out.println((i + 1) + "\t" + error.toString());
			}
		}
		assertTrue(errorList.isEmpty());
		UBL21Writer.order().write(order, out);
		String xml = new String(out.toByteArray(), StandardCharsets.UTF_8);
		System.out.println(xml);
		assertTrue(xml.indexOf(order.getSellerSupplierParty().getParty().getPostalAddress().getCountry().getIdentificationCodeValue()) > 0);
	}

}