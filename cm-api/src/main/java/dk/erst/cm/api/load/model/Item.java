package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;
import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

	@XmlElement(name = "Description", namespace = CBC)
	private String description;

	@XmlElement(name = "PackQuantity", namespace = CBC)
	private UnitQuantity packQuantity;

	@XmlElement(name = "PackSizeNumeric", namespace = CBC)
	private String packSizeNumeric;

	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "Keyword", namespace = CBC)
	private List<String> keywordList;

	@XmlElement(name = "BrandName", namespace = CBC)
	private String brandName;

	@XmlElement(name = "SellersItemIdentification", namespace = CAC)
	private NestedID sellersItemIdentification;

	@XmlElement(name = "ManufacturersItemIdentification", namespace = CAC)
	private NestedID manufacturersItemIdentification;

	@XmlElement(name = "StandardItemIdentification", namespace = CAC)
	private NestedSchemeID standardItemIdentification;

	@XmlElement(name = "ItemSpecificationDocumentReference", namespace = CAC)
	private DocumentReference itemSpecificationDocumentReference;

	@XmlElement(name = "OriginCountry", namespace = CAC)
	private Country originCountry;

	@XmlElement(name = "CommodityClassification", namespace = CAC)
	private List<CommodityClassification> commodityClassification;

	@XmlElement(name = "TransactionConditions", namespace = CAC)
	private TransactionConditions transactionConditions;

	@XmlElement(name = "HazardousItem", namespace = CAC)
	private HazardousItem hazardousItem;

	@XmlElement(name = "ClassifiedTaxCategory", namespace = CAC)
	private ClassifiedTaxCategory classifiedTaxCategory;

	@XmlElement(name = "AdditionalItemProperty", namespace = CAC)
	private List<AdditionalItemProperty> additionalItemPropertyList;

	@XmlElement(name = "ManufacturerParty", namespace = CAC)
	private NestedPartyName manufacturerParty;

	@XmlElement(name = "ItemInstance", namespace = CAC)
	private ItemInstance itemInstance;

	@XmlElement(name = "Certificate", namespace = CAC)
	private Certificate certificate;

	@XmlElement(name = "Dimension", namespace = CAC)
	private List<Dimension> dimensionList;

}
