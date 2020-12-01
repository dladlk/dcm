package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.Const.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Period {

	@XmlElement(name = "StartDate", namespace = CBC)
	private String startDate;

	@XmlElement(name = "EndDate", namespace = CBC)
	private String endDate;
}
