package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class TradingTerms {

	@XmlElement(name = "Information", namespace = CBC)
	private List<String> informationList;
}
