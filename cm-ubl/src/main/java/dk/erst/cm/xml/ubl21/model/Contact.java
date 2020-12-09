package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Contact {

	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "Telephone", namespace = CBC)
	private String telephone;

	@XmlElement(name = "ElectronicMail", namespace = CBC)
	private String electronicMail;

}
