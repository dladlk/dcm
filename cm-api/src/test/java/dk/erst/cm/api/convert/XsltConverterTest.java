package dk.erst.cm.api.convert;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.TransformerException;

import org.junit.jupiter.api.Test;

import dk.erst.cm.api.load.TestDocumentGenerator;
import dk.erst.cm.api.load.TestDocumentGenerator.TestDocumentFile;
import dk.erst.cm.test.TestDocument;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class XsltConverterTest {

	@Test
	void testConvert() throws IOException, TransformerException {
		int[] appendUpToMbList = new int[] { 0, 10, 50, 100, 500 };
		// appendUpToMbList = new int[] { 500 };

		XsltConverter c = new XsltConverter();
		int repeatCount = 5;

		List<Number[]> figuresList = new ArrayList<Number[]>();
		for (int appendUpToMb : appendUpToMbList) {
			TestDocumentFile documentFile = TestDocumentGenerator.getTestDocumentFile(TestDocument.CATALOGUE_PEPPOL, appendUpToMb);
			File tempFile = File.createTempFile(this.getClass().getName(), ".xml");
			long totalDuration = 0;
			for (int i = 0; i < repeatCount; i++) {
				long start = System.currentTimeMillis();
				try (InputStream is = new FileInputStream(documentFile.getFile()); OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile))) {
					c.convert(is, out);
				}
				long duration = System.currentTimeMillis() - start;
				log.info(duration + " " + usedMemory());
				if (i > 0) {
					totalDuration += duration;
				}
				Runtime.getRuntime().gc();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				Runtime.getRuntime().gc();
			}
			long averageDuration = Math.round(totalDuration / (repeatCount - 1));
			int linesCount = documentFile.getLines();
			Number[] figures = new Number[] { linesCount, appendUpToMb, averageDuration, Math.round(linesCount / (averageDuration / 1000.0)) };
			log.info("stat lines\t" + figures[0] + "\tsize\t" + figures[1] + " mb\tduration\t" + figures[2] + "\tspeed\t" + figures[3] + "\tlines/sec");
			figuresList.add(figures);
		}

		System.out.println(String.join(" | ", "Lines", "Size, MB", "Duration, ms", "Lines/sec"));
		System.out.println(String.join(" | ", "---:", "---:", "---:", "---:"));

		for (int i = 0; i < figuresList.size(); i++) {
			Number[] figures = figuresList.get(i);

			String v = Arrays.asList(figures).stream().map(f -> String.valueOf(f)).collect(Collectors.joining(" | "));
			System.out.println(v);
		}
	}

	private static String usedMemory() {
		long bytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return mb(bytes);
	}

	public static String mb(long bytes) {
		return Math.round(bytes / 1024.0 / 1024.0) + " Mb";
	}
}
