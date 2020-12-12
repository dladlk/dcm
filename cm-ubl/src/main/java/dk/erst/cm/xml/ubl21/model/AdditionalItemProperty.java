package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import dk.erst.cm.xml.ubl21.model.annotations.AbsentOIOUBL;
import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class AdditionalItemProperty {

	@Mandatory
	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@AbsentOIOUBL
	@XmlElement(name = "NameCode", namespace = CBC)
	private ListID nameCode;

	@Mandatory
	@XmlElement(name = "Value", namespace = CBC)
	private String value;

	@AbsentOIOUBL
	@XmlElement(name = "ValueQuantity", namespace = CBC)
	private UnitQuantity valueQuantity;

	@AbsentOIOUBL
	@XmlElement(name = "ValueQualifier", namespace = CBC)
	private String valueQualifier;

}
