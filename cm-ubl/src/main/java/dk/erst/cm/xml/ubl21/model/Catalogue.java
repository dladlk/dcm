package dk.erst.cm.xml.ubl21.model;

import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CAC;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CATALOGUE;
import static dk.erst.cm.xml.ubl21.model.NamespacesUBL.CBC;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.erst.cm.xml.ubl21.model.annotations.AbsentOIOUBL;
import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;
import dk.erst.cm.xml.ubl21.model.annotations.MandatoryOIOUBL;
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

	@MandatoryOIOUBL
	@XmlElement(name = "UBLVersionID", namespace = CBC)
	private String ublVersionID;

	@Mandatory
	@XmlElement(name = "CustomizationID", namespace = CBC)
	private String customizationID;

	@Mandatory
	@XmlElement(name = "ProfileID", namespace = CBC)
	private SchemeID profileID;

	@Mandatory
	@XmlElement(name = "ID", namespace = CBC)
	private String id;

	@AbsentOIOUBL
	@XmlElement(name = "ActionCode", namespace = CBC)
	private String actionCode;

	@XmlElement(name = "Name", namespace = CBC)
	private String name;

	@Mandatory
	@XmlElement(name = "IssueDate", namespace = CBC)
	private String issueDate;

	@XmlElement(name = "VersionID", namespace = CBC)
	private String versionID;

	@Mandatory
	@XmlElement(name = "ValidityPeriod", namespace = CAC)
	private Period validityPeriod;

	@XmlElement(name = "ReferencedContract", namespace = CAC)
	private NestedID referencedContract;

	@AbsentOIOUBL
	@XmlElement(name = "SourceCatalogueReference", namespace = CAC)
	private NestedID sourceCatalogueReference;

	@Mandatory
	@XmlElement(name = "ProviderParty", namespace = CAC)
	private Party providerParty;

	@Mandatory
	@XmlElement(name = "ReceiverParty", namespace = CAC)
	private Party receiverParty;

	@XmlElement(name = "SellerSupplierParty", namespace = CAC)
	private NestedParty sellerSupplierParty;

	@XmlElement(name = "ContractorCustomerParty", namespace = CAC)
	private NestedParty contractorCustomerParty;

	@XmlElement(name = "TradingTerms", namespace = CAC)
	private List<TradingTerms> tradingTerms;

	@Mandatory
	@XmlElement(name = "CatalogueLine", namespace = CAC)
	private List<CatalogueLine> lineList;
}
