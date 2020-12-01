
package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.Const.CAC;
import static dk.erst.cm.api.load.model.Const.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemPrice {

	@XmlElement(name = "PriceAmount", namespace = CBC)
	private MoneyAmount priceAmount;

	@XmlElement(name = "BaseQuantity", namespace = CBC)
	private UnitQuantity baseQuantity;

	@XmlElement(name = "PriceType", namespace = CBC)
	private String priceType;

	@XmlElement(name = "OrderableUnitFactorRate", namespace = CBC)
	private String orderableUnitFactorRate;

	@XmlElement(name = "ValidityPeriod", namespace = CAC)
	private Period validityPeriod;
}
