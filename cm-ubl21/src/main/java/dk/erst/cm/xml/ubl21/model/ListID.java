package dk.erst.cm.xml.ubl21.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ListID {

	@XmlValue
	private String id;

	@XmlAttribute(name = "listID")
	private String listID;

	@XmlAttribute(name = "listAgencyID")
	private String listAgencyID;

}
