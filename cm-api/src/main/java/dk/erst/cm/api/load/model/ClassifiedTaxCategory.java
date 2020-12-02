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
public class ClassifiedTaxCategory {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "Percent", namespace = CBC)
	private String percent;

	@XmlElement(name = "TaxScheme", namespace = CAC)
	private NestedID taxScheme;

}
