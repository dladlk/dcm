package dk.erst.cm.api.load;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.handler.CatalogProducer;
import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;
import dk.erst.cm.api.load.model.Item;
import dk.erst.cm.api.load.model.ItemPrice;
import dk.erst.cm.api.load.model.Party;
import dk.erst.cm.api.load.model.RequiredItemLocationQuantity;
import dk.erst.cm.test.TestDocument;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PeppolLoadServiceTest {

	@Test
	@Disabled
	void testLoadXmlPerformance() throws Exception {
		int[] appendUpToMbList = new int[] { 10, 50, 100, 500 };
		// appendUpToMbList = new int[] { 0, 1 };
		int repeatTimes = 5;

		PeppolLoadService s = new PeppolLoadService();

		File tempFile = File.createTempFile(this.getClass().getSimpleName(), ".xml");

		List<Number[]> figuresList = new ArrayList<Number[]>();

		for (int j = 0; j < appendUpToMbList.length; j++) {
			int appendUpToMb = appendUpToMbList[j];

			try {
				log.info("Created temp file for test: " + tempFile.getAbsolutePath());

				int expectedLineCount = 2;

				String xml;
				try (InputStream inputStream = TestDocument.CATALOGUE_PEPPOL.getInputStream()) {
					xml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
				}

				int catalogEnd = xml.lastIndexOf("</Catalogue>");
				int lineStart = xml.lastIndexOf("<cac:CatalogueLine>");
				int lineEnd = catalogEnd - 1;

				String catalogLinePart = xml.substring(lineStart, lineEnd);

				int countAppended = 0;

				try (OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(tempFile)), StandardCharsets.UTF_8)) {
					int curLen = xml.length();
					writer.write(xml.substring(0, lineEnd));

					while (curLen < appendUpToMb * 1024 * 1024) {
						writer.write(catalogLinePart);
						curLen += catalogLinePart.length();
						countAppended++;
					}

					writer.write(xml.substring(catalogEnd));
				}

				expectedLineCount += countAppended;

				log.info("Generated file size " + mb(tempFile.length()) + " after appending " + countAppended + " lines more");

				Runtime.getRuntime().gc();
				log.info("Before unmarshalling: " + usedMemory());
				int lineCount[] = new int[1];

				long totalDuration = 0;
				int repeat = Math.max(1, repeatTimes);
				for (int i = 0; i < repeat; i++) {
					long start = System.currentTimeMillis();

					lineCount[0] = 0;
					try (InputStream inputStream = new FileInputStream(tempFile)) {
						s.loadXml(inputStream, "file://" + tempFile.toPath(), new CatalogConsumer() {
							@Override
							public void consumeLine(CatalogueLine line) {
								lineCount[0]++;
								assertNotNull(line);
							}

							@Override
							public void consumeHead(Catalogue c) {
								assertNotNull(c);
							}
						});
					}
					long duration = System.currentTimeMillis() - start;
					assertEquals(expectedLineCount, lineCount[0]);
					if (i > 0) {
						totalDuration += duration;
					}
					log.info("Done in " + duration + " ms, " + usedMemory() + " used, loaded " + lineCount[0] + " lines, " + Math.round(lineCount[0] / (duration / 1000.0)) + " lines/sec");
					Runtime.getRuntime().gc();
				}
				if (repeat > 1) {
					long averageDuration = Math.round(totalDuration / (repeat - 1));
					Number[] figures = new Number[] { lineCount[0], appendUpToMb, averageDuration, Math.round(lineCount[0] / (averageDuration / 1000.0)) };
					log.info("stat lines\t" + figures[0] + "\tsize\t" + figures[1] + " mb\tduration\t" + figures[2] + "\tspeed\t" + figures[3] + "\tlines/sec");
					figuresList.add(figures);
				}
			} catch (Exception e) {
				log.error("Failed", e);
				throw e;
			} finally {
				assertTrue(FileUtils.deleteQuietly(tempFile));
			}
		}

		System.out.println(String.join(" | ", "Lines", "Size, MB", "Duration, ms", "Lines/sec"));
		System.out.println(String.join(" | ", "---:", "---:", "---:", "---:"));

		for (int i = 0; i < figuresList.size(); i++) {
			Number[] figures = figuresList.get(i);

			String v = Arrays.asList(figures).stream().map(f -> String.valueOf(f)).collect(Collectors.joining(" | "));
			System.out.println(v);
		}
	}

	@Test
	void testLoadXml() throws Exception {
		PeppolLoadService s = new PeppolLoadService();

		final Catalogue[] resCat = new Catalogue[1];
		final List<CatalogueLine> firstLines = new ArrayList<CatalogueLine>();
		try {
			int expectedLineCount = 2;

			Runtime.getRuntime().gc();
			log.info("Before unmarshalling: " + usedMemory());
			int lineCount[] = new int[1];

			long start = System.currentTimeMillis();

			lineCount[0] = 0;
			firstLines.clear();
			try (InputStream inputStream = TestDocument.CATALOGUE_PEPPOL.getInputStream()) {
				s.loadXml(inputStream, TestDocument.CATALOGUE_PEPPOL.getFilePath(), new CatalogConsumer() {
					@Override
					public void consumeLine(CatalogueLine line) {
						if (lineCount[0] < 2) {
							firstLines.add(line);
						}
						assertLine(line);
						lineCount[0]++;
					}

					@Override
					public void consumeHead(Catalogue c) {
						resCat[0] = c;
					}
				});
			}
			long duration = System.currentTimeMillis() - start;
			assertEquals(expectedLineCount, lineCount[0]);
			assertCatalog(resCat[0]);
			log.info("Done in " + duration + " ms, " + usedMemory() + " used, loaded " + lineCount[0] + " lines, " + Math.round(lineCount[0] / (duration / 1000.0)) + " lines/sec");
			Runtime.getRuntime().gc();

			PeppolExportService es = new PeppolExportService();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			resCat[0].setLineList(firstLines);
			es.export(new CatalogProducer() {
				@Override
				public CatalogueLine produceLine() {
					return null;
				}

				@Override
				public Catalogue produceHead() {
					return resCat[0];
				}
			}, baos, true);

			String loadedXml = new String(baos.toByteArray(), StandardCharsets.UTF_8);

			String xml;
			try (InputStream inputStream = TestDocument.CATALOGUE_PEPPOL.getInputStream()) {
				xml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			}
			xml = prettyFormatXml(xml);
			loadedXml = prettyFormatXml(loadedXml);

			assertEquals(xml, loadedXml);

		} catch (Exception e) {
			log.error("Failed", e);
			throw e;
		}
	}

	private void assertCatalog(Catalogue c) {
		assertNotNull(c);
		assertEquals("urn:fdc:peppol.eu:poacc:trns:catalogue:3", c.getCustomizationID());
		assertEquals("urn:fdc:peppol.eu:poacc:bis:catalogue_only:3", c.getProfileID());
		assertEquals("1387", c.getId());
		assertEquals("Add", c.getActionCode());
		assertEquals("Spring Catalogue", c.getName());
		assertEquals("2016-08-01", c.getIssueDate());
		assertEquals("2.0", c.getVersionID());
		assertEquals("2018-09-01", c.getValidityPeriod().getStartDate());
		assertEquals("2019-08-31", c.getValidityPeriod().getEndDate());
		assertEquals("CRT1387", c.getReferencedContract().getId());
		assertEquals("1.0", c.getSourceCatalogueReference().getId());

		Party p = c.getProviderParty();
		assertEquals("987654325", p.getEndpointID().getId());
		assertEquals("0192", p.getEndpointID().getSchemeId());
		assertEquals("5790000435951", p.getPartyIdentification().getId().getId());
		assertEquals("0088", p.getPartyIdentification().getId().getSchemeId());
		assertEquals("Sinsenveien 40", p.getPostalAddress().getStreetName());
		assertEquals("Oppgang B", p.getPostalAddress().getAdditionalStreetName());
		assertEquals("Oslo", p.getPostalAddress().getCityName());
		assertEquals("0501", p.getPostalAddress().getPostalZone());
		assertEquals("Region", p.getPostalAddress().getCountrySubentity());
		assertEquals("Address Line 3", p.getPostalAddress().getAddressLine().get(0).getLine());
		assertEquals("NO", p.getPostalAddress().getCountry().getIdentificationCode());
		assertEquals("Helseforetak AS", p.getPartyLegalEntity().getRegistrationName());
		assertEquals("123456785", p.getPartyLegalEntity().getCompanyID().getId());
		assertEquals("0192", p.getPartyLegalEntity().getCompanyID().getSchemeId());
		assertEquals("Oslo", p.getPartyLegalEntity().getRegistrationAddress().getCityName());
		assertEquals("NO", p.getPartyLegalEntity().getRegistrationAddress().getCountry().getIdentificationCode());

		p = c.getReceiverParty();
		assertEquals("987654325", p.getEndpointID().getId());
		assertEquals("0192", p.getEndpointID().getSchemeId());
		assertEquals("5790000435944", p.getPartyIdentification().getId().getId());
		assertEquals("0088", p.getPartyIdentification().getId().getSchemeId());
		assertEquals("Storgt. 12", p.getPostalAddress().getStreetName());
		assertEquals("4. etasje", p.getPostalAddress().getAdditionalStreetName());
		assertEquals("Oslo", p.getPostalAddress().getCityName());
		assertEquals("0585", p.getPostalAddress().getPostalZone());
		assertEquals("Region", p.getPostalAddress().getCountrySubentity());
		assertEquals("Address Line 3", p.getPostalAddress().getAddressLine().get(0).getLine());
		assertEquals("NO", p.getPostalAddress().getCountry().getIdentificationCode());
		assertEquals("Medical AS", p.getPartyLegalEntity().getRegistrationName());
		assertEquals("123456785", p.getPartyLegalEntity().getCompanyID().getId());
		assertEquals("0192", p.getPartyLegalEntity().getCompanyID().getSchemeId());
		assertEquals("Oslo", p.getPartyLegalEntity().getRegistrationAddress().getCityName());
		assertEquals("NO", p.getPartyLegalEntity().getRegistrationAddress().getCountry().getIdentificationCode());

		p = c.getSellerSupplierParty().getParty();
		assertEquals("987654325", p.getEndpointID().getId());
		assertEquals("0192", p.getEndpointID().getSchemeId());
		assertEquals("5790000435951", p.getPartyIdentification().getId().getId());
		assertEquals("0088", p.getPartyIdentification().getId().getSchemeId());

		assertEquals("Medical", p.getPartyName().getName());

		assertEquals("Storgt. 12", p.getPostalAddress().getStreetName());
		assertEquals("4. etasje", p.getPostalAddress().getAdditionalStreetName());
		assertEquals("Oslo", p.getPostalAddress().getCityName());
		assertEquals("0585", p.getPostalAddress().getPostalZone());
		assertEquals("Region", p.getPostalAddress().getCountrySubentity());
		assertEquals("Address Line 3", p.getPostalAddress().getAddressLine().get(0).getLine());
		assertEquals("NO", p.getPostalAddress().getCountry().getIdentificationCode());
		assertEquals("Nils Nilsen", p.getContact().getName());
		assertEquals("22150510", p.getContact().getTelephone());
		assertEquals("post@medical.no", p.getContact().getElectronicMail());

		p = c.getContractorCustomerParty().getParty();
		assertEquals("123456785", p.getEndpointID().getId());
		assertEquals("0192", p.getEndpointID().getSchemeId());
		assertEquals("5790000435951", p.getPartyIdentification().getId().getId());
		assertEquals("0088", p.getPartyIdentification().getId().getSchemeId());

		assertEquals("Medical", p.getPartyName().getName());

		assertEquals("Nils Nilsen", p.getContact().getName());
		assertEquals("22150510", p.getContact().getTelephone());
		assertEquals("post@medical.no", p.getContact().getElectronicMail());

		assertEquals("Net within 30 days", c.getTradingTerms().get(0).getInformationList().get(0));
	}

	private void assertLine(CatalogueLine line) {
		assertNotNull(line);
		assertNotNull(line.getId());
		if ("1".equals(line.getId())) {
			assertNotNull(line.getActionCode());
			assertEquals("true", line.getOrderableIndicator());
			assertNotNull(line.getContentUnitQuantity());
			assertEquals("10", line.getContentUnitQuantity().getQuantity());
			assertEquals("C62", line.getContentUnitQuantity().getUnitCode());
			assertEquals("1", line.getOrderQuantityIncrementNumeric());
			assertEquals("1", line.getMinimumOrderQuantity().getQuantity());
			assertEquals("LBR", line.getMinimumOrderQuantity().getUnitCode());
			assertEquals("100", line.getMaximumOrderQuantity().getQuantity());
			assertEquals("LBR", line.getMaximumOrderQuantity().getUnitCode());

			assertEquals("text", line.getWarrantyInformation());
			assertEquals("TU", line.getPackLevelCode());

			RequiredItemLocationQuantity ip = line.getItemPrice();
			assertEquals("2", ip.getLeadTimeMeasure().getQuantity());
			assertEquals("DAY", ip.getLeadTimeMeasure().getUnitCode());
			assertEquals("1", ip.getMinimumQuantity().getQuantity());
			assertEquals("LBR", ip.getMinimumQuantity().getUnitCode());
			assertEquals("10", ip.getMaximumQuantity().getQuantity());
			assertEquals("LBR", ip.getMaximumQuantity().getUnitCode());

			ItemPrice p = ip.getPrice();
			assertEquals("10.00", p.getPriceAmount().getAmount());
			assertEquals("EUR", p.getPriceAmount().getCurrencyId());
			assertEquals("1", p.getBaseQuantity().getQuantity());
			assertEquals("C62", p.getBaseQuantity().getUnitCode());
			assertEquals("AAA", p.getPriceType());
			assertEquals("1", p.getOrderableUnitFactorRate());
			assertEquals("2018-10-01", p.getValidityPeriod().getStartDate());
			assertEquals("2018-12-31", p.getValidityPeriod().getEndDate());

			Item item = line.getItem();
			assertEquals("Photo copy paper 80g A4, package of 500 sheets.", item.getDescriptionList().get(0));
			assertEquals("1", item.getPackQuantity().getQuantity());
			assertEquals("LBR", item.getPackQuantity().getUnitCode());
			assertEquals("10", item.getPackSizeNumeric());
			assertEquals("Copy paper", item.getName());
			assertEquals(2, item.getKeywordList().size());
			assertEquals("text", item.getKeywordList().get(0));
			assertEquals("text2", item.getKeywordList().get(1));
			assertEquals("text", item.getBrandNameList().get(0));

		}
		assertNotNull(line.getItem());
	}

	private static String prettyFormatXml(String xml) throws Exception {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = documentBuilder.parse(new InputSource(new StringReader(xml)));

		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(doc);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		xmlString = xmlString.replaceAll("    ", "\t");
		return xmlString;
	}

	private static String usedMemory() {
		long bytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return mb(bytes);
	}

	public static String mb(long bytes) {
		return Math.round(bytes / 1024.0 / 1024.0) + " Mb";
	}
}
