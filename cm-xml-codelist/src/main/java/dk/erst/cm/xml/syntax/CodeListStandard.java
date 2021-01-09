package dk.erst.cm.xml.syntax;

import java.util.LinkedHashMap;
import java.util.Map;

public enum CodeListStandard {

	ActionCode_header("ActionCode_header"),

	ActionCode_line("ActionCode_line"),

	GS17009("GS17009"),

	ICD("ICD"),

	ISO3166_1("ISO3166-1_Alpha2", "ISO3166"),

	ISO4217("ISO4217_2015"),

	Image("Image"),

	MimeCode("MimeCode"),

	TrueFalse("TrueFalse"),

	UNCL1001("UNCL1001"),

	UNCL1001_T01("UNCL1001_T01"),

	UNCL1225("UNCL1225"),

	UNCL1229("UNCL1229"),

	UNCL4343_T76("UNCL4343-T76", "UNCL4343-T76"),

	UNCL5189("UNCL5189"),

	UNCL5305("UNCL5305"),

	UNCL5387("UNCL5387"),

	UNCL6313("UNCL6313"),

	UNCL7143("UNCL7143"),

	UNCL7161("UNCL7161"),

	UNCL8273("UNCL8273"),

	UNECERec20("UNECERec20-11e"),

	EAS("EAS", "eas"),

	EHF1_ActionCode_documentLevel("ehf-postaward-g2/actioncode-documentlevel", "Actioncodedocumentlevel"),

	EHF1_Labels("ehf-postaward-g2/labels", "labels"),
	
	EHF1_Package_Level("ehf-postaward-g2/package-level.xml", "package-level"),
	
	;

	private final String resourceName;
	private final String code;
	
	private static Map<String, CodeListStandard> MAP;

	private CodeListStandard(String resourceName) {
		this(resourceName, null);
	}
	
	private CodeListStandard(String resourceName, String code) {
		this.resourceName = resourceName;
		this.code = code != null ? code : name();
		addToMap(this.code);
	}

	private void addToMap(String codeValue) {
		if (MAP == null) {
			MAP = new LinkedHashMap<String, CodeListStandard>();
		}
		MAP.put(codeValue, this);
	}

	public final String getResourceName() {
		return resourceName;
	}

	public final String getCode() {
		return code;
	}
	
	public static CodeListStandard getInstanceByCode(String code) {
		return MAP.get(code);
	}

}
