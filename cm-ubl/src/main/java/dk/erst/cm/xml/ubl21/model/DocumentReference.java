package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentReference {

	@Mandatory
	@XmlElement(name = "ID", namespace = CBC)
	private String id;
	
	@XmlElement(name = "DocumentTypeCode", namespace = CBC)
	private String documentTypeCode;

	@XmlElement(name = "DocumentDescription", namespace = CBC)
	private List<String> documentDescription;

	@XmlElement(name = "DocumentType", namespace = CBC)
	private String documentType;

	@XmlElement(name = "Attachment", namespace = CAC)
	private Attachment attachment;
}
