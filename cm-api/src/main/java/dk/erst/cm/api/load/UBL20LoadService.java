package dk.erst.cm.api.load;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.handler.CatalogHandler;
import dk.erst.cm.api.load.handler.CatalogHandler.StopParseException;
import dk.erst.cm.xml.ubl20.model.Catalogue;
import dk.erst.cm.xml.ubl20.model.CatalogueLine;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UBL20LoadService {

	private CatalogHandler<Catalogue, CatalogueLine> handler;

	public void loadXml(InputStream xmlInputStream, String description, final CatalogConsumer<Catalogue, CatalogueLine> consumer) throws JAXBException, SAXException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XMLReader reader = factory.newSAXParser().getXMLReader();

		if (handler == null) {
			Unmarshaller headUnmarshaller = JAXBContext.newInstance(Catalogue.class).createUnmarshaller();
			Unmarshaller lineUnmarshaller = JAXBContext.newInstance(CatalogueLine.class).createUnmarshaller();
			handler = new CatalogHandler<Catalogue, CatalogueLine>(headUnmarshaller, lineUnmarshaller);
		}
		handler.setConsumer(consumer);
		reader.setContentHandler(handler);

		try {
			reader.parse(new InputSource(xmlInputStream));
		} catch (StopParseException e) {
			log.info("Loading stopped before lines read");
		} catch (IOException e) {
			log.error("Failed to read " + description, e);
		}
	}

}
