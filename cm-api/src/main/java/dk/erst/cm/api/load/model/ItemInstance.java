package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;
import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemInstance {

	@XmlElement(name = "BestBeforeDate", namespace = CBC)
	private String bestBeforeDate;

	@XmlElement(name = "LotIdentification", namespace = CAC)
	private LotIdentification lotIdentification;

}
