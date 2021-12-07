package dk.erst.cm.api.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.helger.commons.error.IError;
import com.helger.commons.error.list.IErrorList;
import com.helger.ubl21.UBL21Reader;
import com.helger.ubl21.UBL21Validator;
import com.helger.ubl21.UBL21Writer;

import lombok.Data;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.AddressType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CountryType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.CustomerPartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.ItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.LineItemType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.OrderLineType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyIdentificationType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyLegalEntityType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyNameType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PartyType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.PeriodType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.SupplierPartyType;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;

@SuppressWarnings("ConstantConditions")
class OrderProducerTest {

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
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OrderType order = buildOrder();
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

	@SuppressWarnings("SpellCheckingInspection")
	private OrderType buildOrder() {
		OrderType order = new OrderType();
		order.setCustomizationID("urn:fdc:peppol.eu:poacc:trns:order:3");
		order.setProfileID("urn:fdc:peppol.eu:poacc:bis:order_only:3");
		order.setID(UUID.randomUUID().toString());
		order.setIssueDate(LocalDate.now());
		order.setIssueTime(LocalTime.now());
		order.setDocumentCurrencyCode("DKK");
		CustomerPartyType buyerCustomerParty = new CustomerPartyType();
		buyerCustomerParty.setParty(buildParty(new PartyInfo("5798009882806", "0088", "Swedish Company")));
		order.setBuyerCustomerParty(buyerCustomerParty);
		SupplierPartyType supplierPartyType = new SupplierPartyType();
		supplierPartyType.setParty(buildParty(new PartyInfo("5798009882783", "0088", "Danish Company")));
		order.setSellerSupplierParty(supplierPartyType);
		AddressType addressType = new AddressType();
		addressType.setCityName("Stockholm");
		addressType.setPostalZone("2100");
		CountryType countryType = new CountryType();
		countryType.setIdentificationCode("SE");
		addressType.setCountry(countryType);
		supplierPartyType.getParty().setPostalAddress(addressType);
		ArrayList<PeriodType> validityPeriodList = new ArrayList<>();
		PeriodType periodType = new PeriodType();
		periodType.setEndDate(LocalDate.now().plusDays(1));
		validityPeriodList.add(periodType);
		order.setValidityPeriod(validityPeriodList);
		ArrayList<OrderLineType> orderLineList = new ArrayList<>();
		order.setOrderLine(orderLineList);
		OrderLineType line = new OrderLineType();
		LineItemType lineItem = new LineItemType();
		lineItem.setID("1");
		lineItem.setQuantity(BigDecimal.valueOf(1));
		lineItem.getQuantity().setUnitCode("EA");
		ItemType item = new ItemType();
		item.setName("Test");
		ItemIdentificationType itemIdentificationType = new ItemIdentificationType();
		itemIdentificationType.setID("1234");
		item.setSellersItemIdentification(itemIdentificationType);
		lineItem.setItem(item);
		line.setLineItem(lineItem);
		orderLineList.add(line);
		return order;
	}

	public PartyType buildParty(PartyInfo partyInfo) {
		PartyType buyerParty = new PartyType();
		buyerParty.setEndpointID(partyInfo.getEndpointID());
		buyerParty.getEndpointID().setSchemeID(partyInfo.getEndpointIdSchemeID());
		List<PartyIdentificationType> list = new ArrayList<>();
		PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
		partyIdentificationType.setID(partyInfo.getPartyIdentificationID());
		partyIdentificationType.getID().setSchemeID(partyInfo.getPartyIdentificationIDSchemeID());
		list.add(partyIdentificationType);
		buyerParty.setPartyIdentification(list);
		List<PartyNameType> partyNameList = new ArrayList<>();
		PartyNameType e = new PartyNameType();
		e.setName(partyInfo.getPartyName());
		partyNameList.add(e);
		buyerParty.setPartyName(partyNameList);
		List<PartyLegalEntityType> legalEntityList = new ArrayList<>();
		PartyLegalEntityType legalEntity = new PartyLegalEntityType();
		legalEntity.setRegistrationName(partyInfo.getLegalEntityRegistrationName());
		legalEntity.setCompanyID(partyInfo.getLegalEntityCompanyID());
		legalEntity.getCompanyID().setSchemeID(partyInfo.getLegalEntityCompanyIDSchemeID());
		legalEntityList.add(legalEntity);
		buyerParty.setPartyLegalEntity(legalEntityList);
		return buyerParty;
	}

	@Data
	private static class PartyInfo {

		private String endpointID;
		private String endpointIdSchemeID;
		private String partyIdentificationID;
		private String partyIdentificationIDSchemeID;
		private String partyName;
		private String legalEntityRegistrationName;
		private String legalEntityCompanyID;
		private String legalEntityCompanyIDSchemeID;

		public PartyInfo(String defaultId, String defaultSchemeID, String defaultName) {
			this.endpointID = defaultId;
			this.endpointIdSchemeID = defaultSchemeID;
			this.partyIdentificationID = defaultId;
			this.partyIdentificationIDSchemeID = defaultSchemeID;
			this.legalEntityCompanyID = defaultId;
			this.legalEntityCompanyIDSchemeID = defaultSchemeID;
			this.partyName = defaultName;
			this.legalEntityRegistrationName = defaultName;
		}
	}
}