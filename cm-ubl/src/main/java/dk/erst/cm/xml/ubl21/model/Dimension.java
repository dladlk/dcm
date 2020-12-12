package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Dimension {

	@Mandatory
	@XmlElement(name = "AttributeID", namespace = CBC)
	private String attributeID;

	@XmlElement(name = "Measure", namespace = CBC)
	private UnitQuantity Measure;

	@XmlElement(name = "Description", namespace = CBC)
	private List<String> descriptionList;
	
	@XmlElement(name = "MinimumMeasure", namespace = CBC)
	private UnitQuantity minimumMeasure;

	@XmlElement(name = "MaximumMeasure", namespace = CBC)
	private UnitQuantity maximumMeasure;

}
