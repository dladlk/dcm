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
public class ClassifiedTaxCategory {

	@Mandatory
	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "Percent", namespace = CBC)
	private String percent;

	@Mandatory
	@XmlElement(name = "TaxScheme", namespace = CAC)
	private NestedID taxScheme;

}
