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
public class ItemClassificationCode {

	@XmlValue
	private String value;

	@XmlAttribute(name = "listID")
	private String listID;
	@XmlAttribute(name = "listVersionID")
	private String listVersionID;
	@XmlAttribute(name = "name")
	private String name;

}
