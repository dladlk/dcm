package dk.erst.cm.xml.ubl20.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class SchemeID {

	@XmlValue
	private String id;

	@XmlAttribute(name = "schemeAgencyID")
	private String schemeAgencyId;

	@XmlAttribute(name = "schemeID")
	private String schemeId;

	public String getLogicalId() {
		if (schemeId != null) {
			return (schemeId + ":" + id).toUpperCase();
		}
		return id.toUpperCase();
	}

	public String toString() {
		return getLogicalId();
	}
}
