package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class NestedPartyName {

	@XmlElement(name = "PartyName", namespace = CAC)
	private PartyName partyName;

}