package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class RequiredItemLocationQuantity {

	@XmlElement(name = "LeadTimeMeasure", namespace = CBC)
	private UnitQuantity leadTimeMeasure;

	@XmlElement(name = "MinimumQuantity", namespace = CBC)
	private UnitQuantity minimumQuantity;

	@XmlElement(name = "MaximumQuantity", namespace = CBC)
	private UnitQuantity maximumQuantity;

	@XmlElement(name = "ApplicableTerritoryAddress", namespace = CAC)
	private PostalAddress applicableTerritoryAddress;

	@XmlElement(name = "Price", namespace = CAC)
	private ItemPrice price;

}
