package dk.erst.cm.api.load.model;

import static dk.erst.cm.api.load.model.NamespacesUBL.CAC;
import static dk.erst.cm.api.load.model.NamespacesUBL.CATALOGUE;
import static dk.erst.cm.api.load.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlRootElement(name = "Catalogue", namespace = CATALOGUE)
@XmlType(name = "Catalogue")
@XmlAccessorType(XmlAccessType.FIELD)
public class Catalogue {

	@XmlElement(name = "CustomizationID", namespace = CBC)
	private String customizationID;

	@XmlElement(name = "ProfileID", namespace = CBC)
	private String profileID;

	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@XmlElement(name = "ActionCode", namespace = CBC)
	private String actionCode;

	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@XmlElement(name = "IssueDate", namespace = CBC)
	private String issueDate;

	@XmlElement(name = "VersionID", namespace = CBC)
	private String versionID;

	@XmlElement(name = "ValidityPeriod", namespace = CAC)
	private Period validityPeriod;

	@XmlElement(name = "ReferencedContract", namespace = CAC)
	private NestedID referencedContract;

	@XmlElement(name = "SourceCatalogueReference", namespace = CAC)
	private NestedID sourceCatalogueReference;

	@XmlElement(name = "ProviderParty", namespace = CAC)
	private Party providerParty;

	@XmlElement(name = "ReceiverParty", namespace = CAC)
	private Party receiverParty;

	@XmlElement(name = "SellerSupplierParty", namespace = CAC)
	private NestedParty sellerSupplierParty;

	@XmlElement(name = "ContractorCustomerParty", namespace = CAC)
	private NestedParty contractorCustomerParty;

	@XmlElement(name = "TradingTerms", namespace = CAC)
	private TradingTerms tradingTerms;

	@XmlElement(name = "CatalogueLine", namespace = CAC)
	private List<CatalogueLine> lineList;
}
