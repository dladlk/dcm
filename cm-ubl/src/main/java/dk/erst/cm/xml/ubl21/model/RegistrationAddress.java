package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistrationAddress {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "CityName", namespace = CBC)
	private String cityName;

	@XmlElement(name = "Country", namespace = CAC)
	private RegistrationCountry country;
}
