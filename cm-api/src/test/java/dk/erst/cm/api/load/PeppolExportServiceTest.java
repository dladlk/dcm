package dk.erst.cm.api.load;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import com.sun.tools.xjc.util.NullStream;

import dk.erst.cm.api.convert.Peppol2OIOUBLCatalogueConverter;
import dk.erst.cm.api.load.handler.CatalogProducer;
import dk.erst.cm.api.load.handler.ListCatalogConsumer;
import dk.erst.cm.test.TestDocument;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

class PeppolExportServiceTest {

	private static int repeatCount = 1;
	private static int linesCount = 1;
	private Catalogue c;
	private CatalogueLine cl;

	PeppolExportServiceTest() {
		// repeatCount = 10;
		// linesCount = 100 * 1000;

		PeppolLoadService pls = new PeppolLoadService();
		ListCatalogConsumer<Catalogue, CatalogueLine> listCatalogConsumer = new ListCatalogConsumer<Catalogue, CatalogueLine>();
		try {
			pls.loadXml(TestDocument.CATALOGUE_PEPPOL.getInputStream(), null, listCatalogConsumer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		c = listCatalogConsumer.getCatalog();
		cl = listCatalogConsumer.getList().get(0);
	}

	@Test
	void testPush() throws JAXBException, XMLStreamException, IOException {
		XmlStreamMarshaller s = new XmlStreamMarshaller();
		OutputStream baos = repeatCount == 1 ? new ByteArrayOutputStream() : new NullStream();
		long totalDuration = 0;
		System.out.println("Push marshaller with converter");

		Peppol2OIOUBLCatalogueConverter p = new Peppol2OIOUBLCatalogueConverter();

		for (int i = 0; i < repeatCount; i++) {

			s.startMarshall(baos);
			s.marshallHead(p.convert(c));

			long start = System.currentTimeMillis();
			for (int j = 0; j < linesCount; j++) {
				s.marshallLine(p.convert(cl));
			}
			s.endMarshall();
			long duration = System.currentTimeMillis() - start;
			System.out.println("Done in " + duration + ", " + linesCount(duration, linesCount));
			if (i > 0) {
				totalDuration += duration;
			}
			if (repeatCount == 1) {
				String res = new String(((ByteArrayOutputStream) baos).toByteArray(), StandardCharsets.UTF_8);
				System.out.println(res);
				if (linesCount < 1000) {
					FileUtils.write(new File("D:/push.xml"), res, StandardCharsets.UTF_8);
				}
			}

		}
		long avgDuration = Math.round(totalDuration / ((repeatCount - 1.0)));
		System.out.println("Avg duration: " + avgDuration + ", " + linesCount(avgDuration, linesCount));
	}

	private String linesCount(long duration, int linesCount) {
		return (Math.round(linesCount * 1000.0 / duration)) + " lines/sec";
	}

	@Test
	void testPull() throws JAXBException, XMLStreamException, IOException {
		PeppolExportService s = new PeppolExportService(false);
		OutputStream baos = repeatCount == 1 ? new ByteArrayOutputStream() : new NullStream();
		System.out.println("Pull marshaller");

		long totalDuration = 0;

		for (int i = 0; i < repeatCount; i++) {
			long start = System.currentTimeMillis();

			s.export(new CatalogProducer<Catalogue, CatalogueLine>() {

				@Override
				public Catalogue produceHead() {
					return c;
				}

				@Override
				public Iterator<CatalogueLine> lineIterator() {
					return new Iterator<CatalogueLine>() {

						private int count = 0;

						@Override
						public boolean hasNext() {
							return count < linesCount;
						}

						@Override
						public CatalogueLine next() {
							count++;
							return cl;
						}
					};
				}
			}, baos);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Done in " + duration + ", " + linesCount(duration, linesCount));
			if (i > 0) {
				totalDuration += duration;
			}
			if (repeatCount == 1) {
				String res = new String(((ByteArrayOutputStream) baos).toByteArray(), StandardCharsets.UTF_8);
				System.out.println(res);
				if (linesCount < 1000) {
					FileUtils.write(new File("D:/pull.xml"), res, StandardCharsets.UTF_8);
				}
			}

		}

		long avgDuration = Math.round(totalDuration / ((repeatCount - 1.0)));
		System.out.println("Avg duration: " + avgDuration + ", " + linesCount(avgDuration, linesCount));
	}

}
