package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;
import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "CatalogueLine", namespace = CAC)
@XmlType(name = "CatalogueLine")
@XmlAccessorType(XmlAccessType.FIELD)
public class CatalogueLine {

	@Mandatory
	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "ActionCode", namespace = CBC)
	private String actionCode;

	@XmlElement(name = "OrderableIndicator", namespace = CBC)
	private String orderableIndicator;

	@XmlElement(name = "OrderableUnit", namespace = CBC)
	private String orderableUnit;

	@XmlElement(name = "ContentUnitQuantity", namespace = CBC)
	private UnitQuantity contentUnitQuantity;

	@XmlElement(name = "OrderQuantityIncrementNumeric", namespace = CBC)
	private String orderQuantityIncrementNumeric;

	@XmlElement(name = "MinimumOrderQuantity", namespace = CBC)
	private UnitQuantity minimumOrderQuantity;

	@XmlElement(name = "MaximumOrderQuantity", namespace = CBC)
	private UnitQuantity maximumOrderQuantity;

	@XmlElement(name = "WarrantyInformation", namespace = CBC)
	private String warrantyInformation;

	@XmlElement(name = "PackLevelCode", namespace = CBC)
	private String packLevelCode;

	@XmlElement(name = "LineValidityPeriod", namespace = CAC)
	private Period lineValidityPeriod;

	@XmlElement(name = "ItemComparison", namespace = CAC)
	private List<ItemComparison> itemComparison;
	@XmlElement(name = "ComponentRelatedItem", namespace = CAC)
	private List<RelatedItem> componentRelatedItem;
	@XmlElement(name = "AccessoryRelatedItem", namespace = CAC)
	private List<RelatedItem> accessoryRelatedItem;
	@XmlElement(name = "RequiredRelatedItem", namespace = CAC)
	private List<RelatedItem> requiredRelatedItem;

	@XmlElement(name = "RequiredItemLocationQuantity", namespace = CAC)
	private RequiredItemLocationQuantity itemPrice;

	@Mandatory
	@XmlElement(name = "Item", namespace = CAC)
	private Item item;

}
