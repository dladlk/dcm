package dk.erst.cm.api.load;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.handler.CatalogHandler;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PeppolLoadService {

	private CatalogHandler handler;

	public static class StopParseException extends SAXException {

		private static final long serialVersionUID = 5967628673896950778L;
	}

	/*
	 * Example comes from https://github.com/javaee/jaxb-v2/blob/master/jaxb-ri/samples/src/main/samples/partial-unmarshalling/src/Splitter.java
	 */

	public void loadXml(Path xmlPath, final CatalogConsumer consumer) throws JAXBException, SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XMLReader reader = factory.newSAXParser().getXMLReader();

		if (handler == null) {
			handler = new CatalogHandler();
		}
		handler.setConsumer(consumer);
		reader.setContentHandler(handler);

		try (InputStream inputStream = new FileInputStream(xmlPath.toFile())) {
			reader.parse(new InputSource(inputStream));
		} catch (StopParseException e) {
			log.info("Loading stopped before lines read");
		} catch (IOException e) {
			log.error("Failed to read file " + xmlPath, e);
		}
	}

}
