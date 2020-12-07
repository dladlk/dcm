package dk.erst.cm.api.convert;

import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import dk.erst.cm.api.load.PeppolLoadService;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public class ModelConverter {

	private PeppolLoadService peppolLoadService;
	private SerializerCatalogConsumer consumer;

	public ModelConverter() throws JAXBException, XMLStreamException, FactoryConfigurationError {
		peppolLoadService = new PeppolLoadService();

		CatalogueConverter converter = new Peppol2OIOUBLCatalogueConverter();
		consumer = new SerializerCatalogConsumer(converter);
	}

	public static interface CatalogueConverter {
		public Catalogue convert(Catalogue catalogue);

		public CatalogueLine convert(CatalogueLine catalogueLine);
	}

	public void convert(InputStream inputStream, OutputStream outputStream) throws JAXBException, XMLStreamException, SAXException, ParserConfigurationException {
		consumer.start(outputStream);
		peppolLoadService.loadXml(inputStream, null, consumer);
		consumer.finish();
	}
}
