package dk.erst.cm.xml.ubl21.model;

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
public class Country {

	@Mandatory(exceptParents = "cac:ReceiverParty/cac:PartyLegalEntity/cac:RegistrationAddress/cac:Country")
	@XmlElement(name = "IdentificationCode", namespace = CBC)
	private String identificationCode;

}
