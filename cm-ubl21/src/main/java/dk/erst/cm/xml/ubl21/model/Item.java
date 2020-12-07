package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * PEPPOL-T19-B17701 Element 'cbc:Name' MUST be provided.
 * 
 * PEPPOL-T19-R012 Each item in a Catalogue line SHALL be identifiable by either "item sellers identifier" or "item standard identifier" ( https://docs.peppol.eu/poacc/upgrade-3/syntax/Catalogue/cac-CatalogueLine/cac-Item/cac-SellersItemIdentification/ )
 * 
 */
@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	@XmlElement(name = "Description", namespace = CBC)
	private List<String> descriptionList;

	@XmlElement(name = "PackQuantity", namespace = CBC)
	private UnitQuantity packQuantity;

	@XmlElement(name = "PackSizeNumeric", namespace = CBC)
	private String packSizeNumeric;

	@Mandatory
	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "Keyword", namespace = CBC)
	private List<String> keywordList;

	@XmlElement(name = "BrandName", namespace = CBC)
	private List<String> brandNameList;

	@XmlElement(name = "BuyersItemIdentification", namespace = CAC)
	private NestedID buyersItemIdentification;

	@MandatoryOIOUBL // [F-CAT223] One SellersItemIdentification class must be present
	@XmlElement(name = "SellersItemIdentification", namespace = CAC)
	private NestedID sellersItemIdentification;

	@XmlElement(name = "ManufacturersItemIdentification", namespace = CAC)
	private NestedID manufacturersItemIdentification;

	@XmlElement(name = "StandardItemIdentification", namespace = CAC)
	private NestedSchemeID standardItemIdentification;

	@XmlElement(name = "ItemSpecificationDocumentReference", namespace = CAC)
	private List<DocumentReference> itemSpecificationDocumentReferenceList;

	@XmlElement(name = "OriginCountry", namespace = CAC)
	private Country originCountry;

	@MandatoryOIOUBL // [F-CAT230] At least one CommodityClassification class must be present
	@XmlElement(name = "CommodityClassification", namespace = CAC)
	private List<CommodityClassification> commodityClassificationList;

	@XmlElement(name = "TransactionConditions", namespace = CAC)
	private TransactionConditions transactionConditions;

	@XmlElement(name = "HazardousItem", namespace = CAC)
	private List<HazardousItem> hazardousItemList;

	@XmlElement(name = "ClassifiedTaxCategory", namespace = CAC)
	private ClassifiedTaxCategory classifiedTaxCategory;

	@XmlElement(name = "AdditionalItemProperty", namespace = CAC)
	private List<AdditionalItemProperty> additionalItemPropertyList;

	@XmlElement(name = "ManufacturerParty", namespace = CAC)
	private NestedPartyName manufacturerParty;

	@XmlElement(name = "ItemInstance", namespace = CAC)
	private List<ItemInstance> itemInstanceList;

	@AbsentOIOUBL
	@XmlElement(name = "Certificate", namespace = CAC)
	private List<Certificate> certificateList;

	@AbsentOIOUBL
	@XmlElement(name = "Dimension", namespace = CAC)
	private List<Dimension> dimensionList;

}
