package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import dk.erst.cm.xml.ubl21.model.annotations.MandatoryOIOUBL;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class PostalAddress {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@MandatoryOIOUBL
	@XmlElement(name = "AddressFormatCode", namespace = CBC)
	private ListID addressFormatCode;

	@XmlElement(name = "Postbox", namespace = CBC)
	private String postbox;

	@XmlElement(name = "StreetName", namespace = CBC)
	private String streetName;

	@XmlElement(name = "AdditionalStreetName", namespace = CBC)
	private String additionalStreetName;

	@XmlElement(name = "BuildingNumber", namespace = CBC)
	private String buildingNumber;
	
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
