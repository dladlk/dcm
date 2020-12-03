package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;
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
public class PostalAddress {

	@XmlElement(name = "StreetName", namespace = CBC)
	private String streetName;

	@XmlElement(name = "AdditionalStreetName", namespace = CBC)
	private String additionalStreetName;

	@XmlElement(name = "CityName", namespace = CBC)
	private String cityName;

	@XmlElement(name = "PostalZone", namespace = CBC)
	private String postalZone;

	@XmlElement(name = "CountrySubentity", namespace = CBC)
	private String countrySubentity;

	@XmlElement(name = "AddressLine", namespace = CAC)
	private List<Line> addressLine;

	@Mandatory
	@XmlElement(name = "Country", namespace = CAC)
	private Country country;

}
