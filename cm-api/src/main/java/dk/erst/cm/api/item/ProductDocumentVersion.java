package dk.erst.cm.api.item;

public enum ProductDocumentVersion {

	PEPPOL_CATALOGUE_3_1("PEPPOL Catalogue transaction 3.1 (T19)"),

	;

	private final String version;

	private ProductDocumentVersion(String version) {
		this.version = version;
	}

	public final String getVersion() {
		return version;
	}

}
