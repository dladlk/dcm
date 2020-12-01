package dk.erst.cm.api.load.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import static dk.erst.cm.api.load.model.Const.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "Catalogue", namespace = CATALOGUE)
@XmlType(name = "Catalogue", propOrder = {"id", "issueDate"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Catalogue {

	@XmlElement(name = "ID", namespace = CBC)
	private String id;
	@XmlElement(name = "IssueDate", namespace = CBC)
	private String issueDate;
	
}
