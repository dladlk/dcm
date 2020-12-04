package dk.erst.cm.xml.ubl20.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitQuantity {

	@XmlValue
	private String quantity;

	@Mandatory
	@XmlAttribute(name = "unitCode")
	private String unitCode;

}
