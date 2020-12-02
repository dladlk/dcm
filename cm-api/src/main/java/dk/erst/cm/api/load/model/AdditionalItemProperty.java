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
public class AdditionalItemProperty {

	@Mandatory
	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "NameCode", namespace = CBC)
	private ListID nameCode;

	@Mandatory
	@XmlElement(name = "Value", namespace = CBC)
	private String value;

	@XmlElement(name = "ValueQuantity", namespace = CBC)
	private UnitQuantity valueQuantity;

	@XmlElement(name = "ValueQualifier", namespace = CBC)
	private String valueQualifier;

}
