package dk.erst.cm.api.load;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.handler.CatalogProducer;
import dk.erst.cm.test.TestDocument;
import dk.erst.cm.xml.ubl20.model.Catalogue;
import dk.erst.cm.xml.ubl20.model.CatalogueLine;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UBL20LoadServiceTest {

	@Test
	void testLoadXml() throws Exception {
		UBL20LoadService s = new UBL20LoadService();
		UBL20ExportService es = new UBL20ExportService();
		TestDocument testDocument = TestDocument.CATALOGUE_OIOUBL;

		final Catalogue[] resCat = new Catalogue[1];
		final List<CatalogueLine> firstLines = new ArrayList<CatalogueLine>();
		try {
			int expectedLineCount = 3;

			Runtime.getRuntime().gc();
			log.info("Before unmarshalling: " + usedMemory());
			int lineCount[] = new int[1];

			long start = System.currentTimeMillis();

			lineCount[0] = 0;
			firstLines.clear();
			try (InputStream inputStream = testDocument.getInputStream()) {
				s.loadXml(inputStream, testDocument.getFilePath(), new CatalogConsumer<Catalogue, CatalogueLine>() {
					@Override
					public void consumeLine(CatalogueLine line) {
						if (lineCount[0] < expectedLineCount) {
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

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			resCat[0].setLineList(firstLines);
			es.export(new CatalogProducer<Catalogue, CatalogueLine>() {

				@Override
				public Catalogue produceHead() {
					return resCat[0];
				}

				@Override
				public Iterator<CatalogueLine> lineIterator() {
					return resCat[0].getLineList().iterator();
				}
			}, baos, true);

			String loadedXml = new String(baos.toByteArray(), StandardCharsets.UTF_8);

			String xml;
			try (InputStream inputStream = testDocument.getInputStream()) {
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
	}

	private void assertLine(CatalogueLine line) {
		assertNotNull(line);
		assertNotNull(line.getId());
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
