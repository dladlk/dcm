package dk.erst.cm.xml.ubl21.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class SchemeID {

	@XmlValue
	private String id;

	@XmlAttribute(name = "schemeAgencyID")
	private String schemeAgencyId;

	@Mandatory(exceptParents = { "cac:PartyIdentification/cbc:ID", "cac:PartyLegalEntity/cbc:CompanyID" })
	@XmlAttribute(name = "schemeID")
	private String schemeId;

}