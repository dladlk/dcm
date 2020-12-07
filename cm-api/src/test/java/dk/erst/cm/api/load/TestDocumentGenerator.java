package dk.erst.cm.api.load;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.springframework.util.StreamUtils;

import dk.erst.cm.test.TestDocument;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDocumentGenerator {

	@Data
	public static class TestDocumentFile {
		private File file;
		private int lines;
		private TestDocument testDocument;
		private int sizeMB;
	}

	public static TestDocumentFile getTestDocumentFile(TestDocument testDocument, int sizeMB) throws IOException {
		File tempFolder = new File(System.getProperty("java.io.tmpdir"));

		String fileNamePrefix = testDocument.getClass().getName() + "_" + testDocument.name() + "_" + sizeMB + "_MB";
		String globPattern = fileNamePrefix + "_*_lines.xml";

		Path testDocumentPath = null;
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(tempFolder.toPath(), globPattern)) {
			Iterator<Path> iterator = stream.iterator();
			if (iterator.hasNext()) {
				testDocumentPath = iterator.next();
			}
		}

		File testDocumentFile;
		int lines = 0;
		if (testDocumentPath == null) {
			String xml;
			try (InputStream inputStream = testDocument.getInputStream()) {
				xml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
			}

			int catalogEnd = xml.lastIndexOf("</Catalogue>");
			int lineStart = xml.lastIndexOf("<cac:CatalogueLine>");
			int lineEnd = catalogEnd - 1;

			String catalogLinePart = xml.substring(lineStart, lineEnd);

			int countAppended = 0;

			File tempFile = File.createTempFile(TestDocumentGenerator.class.getSimpleName(), ".xml");

			try (OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(tempFile)), StandardCharsets.UTF_8)) {
				int curLen = xml.length();
				writer.write(xml.substring(0, lineEnd));

				while (curLen < sizeMB * 1024 * 1024) {
					writer.write(catalogLinePart);
					curLen += catalogLinePart.length();
					countAppended++;
				}

				writer.write(xml.substring(catalogEnd));
			}
			lines = countAppended + 2;
			testDocumentFile = new File(tempFolder, fileNamePrefix + "_" + (lines) + "_lines.xml");
			tempFile.renameTo(testDocumentFile);
			log.info("Generated test file " + testDocumentFile);
		} else {
			log.info("Found existing test file " + testDocumentPath);
			testDocumentFile = testDocumentPath.toFile();
			String fileName = testDocumentPath.getFileName().toString();
			int linesEnd = fileName.lastIndexOf("_lines");
			int linesStart = fileName.lastIndexOf("_", linesEnd - 1);
			lines = Integer.parseInt(fileName.substring(linesStart + 1, linesEnd));
		}

		TestDocumentFile res = new TestDocumentFile();
		res.setFile(testDocumentFile);
		res.setLines(lines);
		res.setTestDocument(testDocument);
		res.setSizeMB(sizeMB);
		return res;
	}

	@Test
	public void test() throws IOException {
		getTestDocumentFile(TestDocument.CATALOGUE_PEPPOL, 0);
		getTestDocumentFile(TestDocument.CATALOGUE_PEPPOL, 0);
		getTestDocumentFile(TestDocument.CATALOGUE_PEPPOL, 1);
		getTestDocumentFile(TestDocument.CATALOGUE_PEPPOL, 1);
	}

}
