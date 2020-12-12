
package dk.erst.cm.xml.ubl21.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class MoneyAmount {

	@XmlValue
	private String amount;

	@Mandatory
	@XmlAttribute(name = "currencyID")
	private String currencyId;

}
