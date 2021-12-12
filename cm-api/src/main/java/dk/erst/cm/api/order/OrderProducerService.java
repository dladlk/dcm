package dk.erst.cm.api.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.order.data.CustomerOrderData;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
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

@Service
public class OrderProducerService {

	@Data
	public static class PartyInfo {

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

	public OrderType generateOrder(Order dataOrder, CustomerOrderData customerOrderData, List<Product> productList) {
		OrderType order = new OrderType();

		order.setCustomizationID("urn:fdc:peppol.eu:poacc:trns:order:3");
		order.setProfileID("urn:fdc:peppol.eu:poacc:bis:order_only:3");
		order.setID(UUID.randomUUID().toString());
		order.setIssueDate(dataOrder.getCreateTime().atZone(ZoneOffset.UTC).toLocalDate());
		order.setIssueTime(dataOrder.getCreateTime().atZone(ZoneOffset.UTC).toLocalTime());
		order.setDocumentCurrencyCode("DKK");

		CustomerPartyType buyerCustomerParty = new CustomerPartyType();
		buyerCustomerParty.setParty(buildParty(new PartyInfo("5798009882806", "0088", customerOrderData.getBuyerCompany().getRegistrationName())));
		order.setBuyerCustomerParty(buyerCustomerParty);

		SupplierPartyType supplierPartyType = new SupplierPartyType();
		supplierPartyType.setParty(buildParty(new PartyInfo("5798009882783", "0088", "Danish Company")));
		order.setSellerSupplierParty(supplierPartyType);

		AddressType supplierAddress = new AddressType();
		supplierAddress.setCityName("Stockholm");
		supplierAddress.setPostalZone("2100");
		CountryType countryType = new CountryType();
		countryType.setIdentificationCode("SE");
		supplierAddress.setCountry(countryType);
		supplierPartyType.getParty().setPostalAddress(supplierAddress);

		ArrayList<PeriodType> validityPeriodList = new ArrayList<>();
		PeriodType periodType = new PeriodType();
		periodType.setEndDate(LocalDate.now().plusDays(1));
		validityPeriodList.add(periodType);
		order.setValidityPeriod(validityPeriodList);

		ArrayList<OrderLineType> orderLineList = new ArrayList<>();
		order.setOrderLine(orderLineList);

		for (int i = 0; i < productList.size(); i++) {
			Product product = productList.get(i);

			CatalogueLine catalogueLine = (CatalogueLine) product.getDocument();

			OrderLineType line = new OrderLineType();
			LineItemType lineItem = new LineItemType();
			lineItem.setID(product.getId());
			lineItem.setQuantity(BigDecimal.valueOf(1));
			lineItem.getQuantity().setUnitCode("EA");
			ItemType item = new ItemType();
			item.setName(catalogueLine.getItem().getName());

			ItemIdentificationType itemIdentificationType = new ItemIdentificationType();
			itemIdentificationType.setID(catalogueLine.getItem().getSellersItemIdentification().getId());
			item.setSellersItemIdentification(itemIdentificationType);

			lineItem.setItem(item);
			line.setLineItem(lineItem);
			orderLineList.add(line);
		}

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

}
