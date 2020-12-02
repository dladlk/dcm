package dk.erst.cm.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public enum TestDocument {

	CATALOGUE_PEPPOL("Peppol_Catalogue_Example.xml"),

	;

	private static String TEST_EXAMPLE_ROOT_PATH = "../cm-resources/examples/";

	private final String filename;

	private TestDocument(String filename) {
		this.filename = filename.trim();
	}

	public String getFilePath() {
		return TEST_EXAMPLE_ROOT_PATH + this.filename;
	}

	public InputStream getInputStream() {
		try {
			return new FileInputStream(this.getFilePath());
		} catch (FileNotFoundException e) {
			System.err.println("File " + this.getFilePath() + " is not found.");
		}
		return null;
	}

	public boolean isExpectedSuccess() {
		return !this.name().startsWith("ERROR_");
	}
}
