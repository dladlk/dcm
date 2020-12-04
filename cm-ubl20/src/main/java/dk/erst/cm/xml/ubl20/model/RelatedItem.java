package dk.erst.cm.xml.ubl20.model;

import static dk.erst.cm.xml.ubl20.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class RelatedItem {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "Quantity", namespace = CBC)
	private UnitQuantity quantity;

}
