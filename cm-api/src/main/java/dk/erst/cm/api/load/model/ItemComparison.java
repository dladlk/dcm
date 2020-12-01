package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemComparison {

	@XmlElement(name = "PriceAmount", namespace = CBC)
	private MoneyAmount priceAmount;

	@XmlElement(name = "Quantity", namespace = CBC)
	private UnitQuantity quantity;

}
