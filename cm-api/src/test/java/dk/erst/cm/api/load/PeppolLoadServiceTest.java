package dk.erst.cm.api.load;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;
import dk.erst.cm.api.load.model.Item;
import dk.erst.cm.api.load.model.ItemPrice;
import dk.erst.cm.api.load.model.RequiredItemLocationQuantity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class PeppolLoadServiceTest {

	int appendUpToMb = 0;
	int repeatTimes = 0;

	@Test
	void testLoadXml() throws Exception {
		appendUpToMb = 500;
		repeatTimes = 5;

		PeppolLoadService s = new PeppolLoadService();

		File tempFile = File.createTempFile(this.getClass().getSimpleName(), ".xml");

		try {
			log.info("Created temp file for test: " + tempFile.getAbsolutePath());

			int expectedLineCount = 2;

			if (appendUpToMb > 0) {

				String xml;
				try (InputStream inputStream = this.getClass().getResourceAsStream("/Catalogue_Example.xml")) {
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

			} else {
				try (InputStream inputStream = this.getClass().getResourceAsStream("/Catalogue_Example.xml"); OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile))) {
					StreamUtils.copy(inputStream, outputStream);
				}
			}

			Runtime.getRuntime().gc();
			log.info("Before unmarshalling: " + usedMemory());
			int lineCount[] = new int[1];

			long totalDuration = 0;
			int repeat = Math.max(1, repeatTimes);
			for (int i = 0; i < repeat; i++) {
				long start = System.currentTimeMillis();

				lineCount[0] = 0;

				s.loadXml(tempFile.toPath(), new CatalogConsumer() {
					@Override
					public void consumeLine(CatalogueLine line) {
						assertLine(line);
						lineCount[0]++;
					}

					@Override
					public void consumeHead(Catalogue c) {
						assertNotNull(c);
						assertEquals("1387", c.getId());
						assertEquals("2016-08-01", c.getIssueDate());
					}
				});
				assertEquals(expectedLineCount, lineCount[0]);
				long duration = System.currentTimeMillis() - start;
				if (i > 0) {
					totalDuration += duration;
				}
				log.info("Done in " + duration + " ms, " + usedMemory() + " used, loaded " + lineCount[0] + " lines, " + Math.round(lineCount[0] / (duration / 1000.0)) + " lines/sec");
				Runtime.getRuntime().gc();
			}
			if (repeat > 1) {
				long averageDuration = Math.round(totalDuration / (repeat - 1));
				log.info("lines\t" + lineCount[0] + "\tsize\t" + appendUpToMb + " mb\tduration\t" + averageDuration + "\tspeed\t" + Math.round(lineCount[0] / (averageDuration / 1000.0)) + "\tlines/sec");
			}
		} catch (Exception e) {
			log.error("Failed", e);
			throw e;
		} finally {
			assertTrue(FileUtils.deleteQuietly(tempFile));
		}
	}

	private void assertLine(CatalogueLine line) {
		assertNotNull(line);
		assertNotNull(line.getId());
		if ("1".equals(line.getId())) {
			assertNotNull(line.getActionCode());
			assertTrue(line.isOrderableIndicator());
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
			assertEquals("Photo copy paper 80g A4, package of 500 sheets.", item.getDescription());
			assertEquals("1", item.getPackQuantity().getQuantity());
			assertEquals("LBR", item.getPackQuantity().getUnitCode());
			assertEquals("10", item.getPackSizeNumeric());
			assertEquals("Copy paper", item.getName());
			assertEquals(2, item.getKeywordList().size());
			assertEquals("text", item.getKeywordList().get(0));
			assertEquals("text2", item.getKeywordList().get(1));
			assertEquals("text", item.getBrandName());

		}
		assertNotNull(line.getItem());
	}

	private static String usedMemory() {
		long bytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return mb(bytes);
	}

	public static String mb(long bytes) {
		return Math.round(bytes / 1024.0 / 1024.0) + " Mb";
	}
}
