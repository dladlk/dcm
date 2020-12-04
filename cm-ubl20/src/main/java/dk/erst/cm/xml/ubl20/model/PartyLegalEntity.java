package dk.erst.cm.xml.ubl20.model;

import static dk.erst.cm.xml.ubl20.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl20.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class PartyLegalEntity {

	@XmlElement(name = "RegistrationName", namespace = CBC)
	private String registrationName;

	@XmlElement(name = "CompanyID", namespace = CBC)
	private SchemeID CompanyID;

	@XmlElement(name = "RegistrationAddress", namespace = CAC)
	private PostalAddress registrationAddress;

}
