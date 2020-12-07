package dk.erst.cm.api.convert;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.jupiter.api.Test;

import dk.erst.cm.test.TestDocument;

class ModelConverterTest {

	private boolean validateOriginal = true;

	private String resourcesRoot = "/wsd/delis/delis-resources/validation";

	XmlValidator validator = new XmlValidator();

	private static interface InputStreamSource {
		public InputStream getInputStream() throws IOException;

		public String getDescription();
	}

	@Test
	void testConvert() throws Exception {
		TestDocument testDocument = TestDocument.CATALOGUE_PEPPOL;
		ModelConverter mc = new ModelConverter();
		convertValidate(mc, new InputStreamSource() {
			@Override
			public InputStream getInputStream() {
				return testDocument.getInputStream();
			}

			@Override
			public String getDescription() {
				return testDocument.name();
			}
		});

		if (testDocument.getAdditionalExamplesFolder() != null) {
			File folder = new File(testDocument.getAdditionalExamplesFolder());
			File[] examples = folder.listFiles();
			for (final File file : examples) {
				convertValidate(mc, new InputStreamSource() {
					@Override
					public InputStream getInputStream() throws IOException {
						return new FileInputStream(file);
					}

					@Override
					public String getDescription() {
						return file.getAbsolutePath();
					}
				});
			}
		}
	}

	private void convertValidate(ModelConverter mc, InputStreamSource iss) throws Exception {
		if (validateOriginal) {
			String testDesc = iss.getDescription();
			try (InputStream is = iss.getInputStream()) {
				assertTrue(validator.validateByXsd(is, testDesc, resourcesRoot + "/xsd/UBL_2.1/maindoc/UBL-Catalogue-2.1.xsd"));
			}
			try (InputStream is = iss.getInputStream()) {
				assertTrue(validator.validateBySchematron(is, testDesc, resourcesRoot + "/sch/bis3/other/2020-11-16_v1.2.1/PEPPOLBIS-T19.xslt"));
			}
		}

		File tempFile = new File("D:/output.xml");
		try (InputStream is = iss.getInputStream(); OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile), 20 * 1024)) {
			mc.convert(is, out);
		}

		String testDesc = tempFile.getCanonicalPath();
		try (InputStream is = new FileInputStream(tempFile)) {
			boolean validXSD = validator.validateByXsd(is, testDesc, resourcesRoot + "/xsd/UBL_2.0/maindoc/UBL-Catalogue-2.0.xsd");
			assertTrue(validXSD);
		}
		try (InputStream is = new FileInputStream(tempFile)) {
			assertTrue(validator.validateBySchematron(is, testDesc, resourcesRoot + "/sch/oioubl/OIOUBL_Schematron_2019-04-08_v1.11.1.35666/OIOUBL_Catalogue_Schematron.xsl"));
		}
	}

}
