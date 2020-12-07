package dk.erst.cm.api.load;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class DelegatingXMLStreamWriter implements XMLStreamWriter {

	private XMLStreamWriter xmlStreamWriter;

	public DelegatingXMLStreamWriter(XMLStreamWriter xmlStreamWriter) {
		this.xmlStreamWriter = xmlStreamWriter;

	}

	public void writeStartElement(String localName) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(localName);
	}

	public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(namespaceURI, localName);
	}

	public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(prefix, localName, namespaceURI);
	}

	public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
		xmlStreamWriter.writeEmptyElement(namespaceURI, localName);
	}

	public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
		xmlStreamWriter.writeEmptyElement(prefix, localName, namespaceURI);
	}

	public void writeEmptyElement(String localName) throws XMLStreamException {
		xmlStreamWriter.writeEmptyElement(localName);
	}

	public void writeEndElement() throws XMLStreamException {
		xmlStreamWriter.writeEndElement();
	}

	public void writeEndDocument() throws XMLStreamException {
		xmlStreamWriter.writeEndDocument();
	}

	public void close() throws XMLStreamException {
		xmlStreamWriter.close();
	}

	public void flush() throws XMLStreamException {
		xmlStreamWriter.flush();
	}

	public void writeAttribute(String localName, String value) throws XMLStreamException {
		xmlStreamWriter.writeAttribute(localName, value);
	}

	public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
		xmlStreamWriter.writeAttribute(prefix, namespaceURI, localName, value);
	}

	public void writeAttribute(String namespaceURI, String localName, String value) throws XMLStreamException {
		xmlStreamWriter.writeAttribute(namespaceURI, localName, value);
	}

	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		xmlStreamWriter.writeNamespace(prefix, namespaceURI);
	}

	public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
		xmlStreamWriter.writeDefaultNamespace(namespaceURI);
	}

	public void writeComment(String data) throws XMLStreamException {
		xmlStreamWriter.writeComment(data);
	}

	public void writeProcessingInstruction(String target) throws XMLStreamException {
		xmlStreamWriter.writeProcessingInstruction(target);
	}

	public void writeProcessingInstruction(String target, String data) throws XMLStreamException {
		xmlStreamWriter.writeProcessingInstruction(target, data);
	}

	public void writeCData(String data) throws XMLStreamException {
		xmlStreamWriter.writeCData(data);
	}

	public void writeDTD(String dtd) throws XMLStreamException {
		xmlStreamWriter.writeDTD(dtd);
	}

	public void writeEntityRef(String name) throws XMLStreamException {
		xmlStreamWriter.writeEntityRef(name);
	}

	public void writeStartDocument() throws XMLStreamException {
		xmlStreamWriter.writeStartDocument();
	}

	public void writeStartDocument(String version) throws XMLStreamException {
		xmlStreamWriter.writeStartDocument(version);
	}

	public void writeStartDocument(String encoding, String version) throws XMLStreamException {
		xmlStreamWriter.writeStartDocument(encoding, version);
	}

	public void writeCharacters(String text) throws XMLStreamException {
		xmlStreamWriter.writeCharacters(text);
	}

	public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
		xmlStreamWriter.writeCharacters(text, start, len);
	}

	public String getPrefix(String uri) throws XMLStreamException {
		return xmlStreamWriter.getPrefix(uri);
	}

	public void setPrefix(String prefix, String uri) throws XMLStreamException {
		xmlStreamWriter.setPrefix(prefix, uri);
	}

	public void setDefaultNamespace(String uri) throws XMLStreamException {
		xmlStreamWriter.setDefaultNamespace(uri);
	}

	public void setNamespaceContext(NamespaceContext context) throws XMLStreamException {
		xmlStreamWriter.setNamespaceContext(context);
	}

	public NamespaceContext getNamespaceContext() {
		return xmlStreamWriter.getNamespaceContext();
	}

	public Object getProperty(String name) throws IllegalArgumentException {
		return xmlStreamWriter.getProperty(name);
	}
}
