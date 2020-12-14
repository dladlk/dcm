package dk.erst.cm.xml.syntax;

public enum CodeListStandard {

	ActionCode_header("ActionCode_header"),

	ActionCode_line("ActionCode_line"),

	GS17009("GS17009"),

	ICD("ICD"),

	ISO3166_1("ISO3166-1_Alpha2"),

	ISO4217("ISO4217_2015"),

	Image("Image"),

	MimeCode("MimeCode"),

	TrueFalse("TrueFalse"),

	UNCL1001("UNCL1001"),

	UNCL5305("UNCL5305"),

	UNCL5387("UNCL5387"),

	UNCL6313("UNCL6313"),

	UNCL7143("UNCL7143"),

	UNCL8273("UNCL8273"),

	UNECERec20("UNECERec20-11e"),

	EAS("eas"),

	;

	private final String resourceName;

	private CodeListStandard(String resourceName) {
		this.resourceName = resourceName;

	}

	public final String getResourceName() {
		return resourceName;
	}

}
