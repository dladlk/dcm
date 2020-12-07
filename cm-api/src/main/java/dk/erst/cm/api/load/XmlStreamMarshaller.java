package dk.erst.cm.api.load;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import dk.erst.cm.api.load.PeppolExportService.UblNamespacePrefixMapper;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public class XmlStreamMarshaller {

	private Marshaller headMarshaller;
	private Marshaller lineMarshaller;

	private XMLStreamWriter xsw;
	private HeadWriter headWriter;
	private LineWriter lineWriter;

	public XmlStreamMarshaller() throws JAXBException {
		headMarshaller = JAXBContext.newInstance(Catalogue.class).createMarshaller();
		headMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
		headMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new UblNamespacePrefixMapper());

		lineMarshaller = JAXBContext.newInstance(CatalogueLine.class).createMarshaller();
		lineMarshaller.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.displayName());
		lineMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		lineMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new UblNamespacePrefixMapper());
	}

	public void startMarshall(OutputStream out) throws XMLStreamException, FactoryConfigurationError {
		xsw = XMLOutputFactory.newInstance().createXMLStreamWriter(out);
		headWriter = new HeadWriter(xsw);
		lineWriter = new LineWriter(xsw);
	}

	public void marshallHead(Catalogue head) throws JAXBException, XMLStreamException {
		headMarshaller.marshal(head, headWriter);
	}

	public void marshallLine(CatalogueLine line) throws JAXBException {
		lineMarshaller.marshal(line, lineWriter);
	}

	public void endMarshall() throws XMLStreamException {
		headWriter.writeLastElement();
		headWriter.writeEndDocument();
	}

}
