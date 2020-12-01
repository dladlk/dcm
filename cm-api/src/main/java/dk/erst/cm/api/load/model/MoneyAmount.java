
package dk.erst.cm.api.load.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class MoneyAmount {

	@XmlValue
	private String amount;

	@XmlAttribute(name = "currencyID")
	private String currencyId;

}
