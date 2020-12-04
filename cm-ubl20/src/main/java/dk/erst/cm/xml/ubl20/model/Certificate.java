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
public class Certificate {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "CertificateTypeCode", namespace = CBC)
	private String certificateTypeCode;

	@XmlElement(name = "CertificateType", namespace = CBC)
	private String certificateType;

	@XmlElement(name = "Remarks", namespace = CBC)
	private String remarks;

	@XmlElement(name = "IssuerParty", namespace = CAC)
	private NestedPartyName issuerParty;

}
