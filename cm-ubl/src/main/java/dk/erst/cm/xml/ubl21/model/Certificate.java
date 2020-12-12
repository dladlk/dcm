package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Certificate {

	@Mandatory
	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@Mandatory
	@XmlElement(name = "CertificateTypeCode", namespace = CBC)
	private String certificateTypeCode;

	@Mandatory
	@XmlElement(name = "CertificateType", namespace = CBC)
	private String certificateType;

	@XmlElement(name = "Remarks", namespace = CBC)
	private String remarks;

	@Mandatory
	@XmlElement(name = "IssuerParty", namespace = CAC)
	private NestedPartyName issuerParty;

	@XmlElement(name = "DocumentReference", namespace = CAC)
	private DocumentReference documentReference;

}
