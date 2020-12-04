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
public class Party {

	@Mandatory
	@XmlElement(name = "EndpointID", namespace = CBC)
	private SchemeID endpointID;

	@XmlElement(name = "PartyIdentification", namespace = CAC)
	private PartyIdentification partyIdentification;

	@XmlElement(name = "PartyName", namespace = CAC)
	private PartyName partyName;

	@XmlElement(name = "PostalAddress", namespace = CAC)
	private PostalAddress postalAddress;

	@XmlElement(name = "PartyLegalEntity", namespace = CAC)
	private PartyLegalEntity partyLegalEntity;

	@XmlElement(name = "Contact", namespace = CAC)
	private Contact contact;

}
