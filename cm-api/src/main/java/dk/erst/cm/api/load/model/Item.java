package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.Const.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
	
	@XmlElement(name = "Description", namespace = CBC)
	private String description;
	
	@XmlElement(name = "PackQuantity", namespace = CBC)
	private UnitQuantity packQuantity;

	@XmlElement(name = "PackSizeNumeric", namespace = CBC)
	private String packSizeNumeric;

	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "Keyword", namespace = CBC)
	private List<String> keywordList;

	@XmlElement(name = "BrandName", namespace = CBC)
	private String brandName;

}
