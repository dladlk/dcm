package dk.erst.cm.api.load;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class HeadWriter extends DelegatingXMLStreamWriter {

	private int level;

	public HeadWriter(XMLStreamWriter xmlStreamWriter) {
		super(xmlStreamWriter);
	}

	@Override
	public void writeStartElement(String localName) throws XMLStreamException {
		level++;
		super.writeStartElement(localName);
	}

	@Override
	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		level++;
		super.writeStartElement(namespaceURI, localName);
	}

	@Override
	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		level++;
		super.writeStartElement(prefix, localName, namespaceURI);
	}

	@Override
	public void writeEndElement() throws XMLStreamException {
		level--;
		if (level != 0) {
			super.writeEndElement();
		}
	}

	public void writeLastElement() throws XMLStreamException {
		super.writeEndElement();
	}

	@Override
	public void writeEndDocument() throws XMLStreamException {
		/*
		 * Do nothing
		 */
	}

}
