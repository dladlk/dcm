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
public class EmbeddedDocumentBinaryObject {

	@XmlAttribute(name = "mimeCode")
	private String mimeCode;

	@XmlAttribute(name = "filename")
	private String filename;

	@XmlValue
	private String value;

}
