package dk.erst.cm.api.convert;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import dk.erst.cm.api.convert.ModelConverter.CatalogueConverter;
import dk.erst.cm.api.load.XmlStreamMarshaller;
import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public class SerializerCatalogConsumer implements CatalogConsumer<Catalogue, CatalogueLine> {

	private XmlStreamMarshaller exportService;
	private CatalogueConverter converter;

	public SerializerCatalogConsumer(CatalogueConverter converter) throws JAXBException, XMLStreamException, FactoryConfigurationError {
		this.converter = converter;
		this.exportService = new XmlStreamMarshaller();
	}

	public void start(OutputStream out) throws XMLStreamException {
		exportService.startMarshall(out);
	}

	@Override
	public void consumeHead(Catalogue catalog) {
		try {
			exportService.marshallHead(converter.convert(catalog));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void consumeLine(CatalogueLine line) {
		try {
			exportService.marshallLine(converter.convert(line));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void finish() throws XMLStreamException {
		exportService.endMarshall();
	}

}