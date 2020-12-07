package dk.erst.cm.api.load;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class LineWriter extends DelegatingXMLStreamWriter {

	public LineWriter(XMLStreamWriter xmlStreamWriter) {
		super(xmlStreamWriter);
	}

	@Override
	public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
		if ("".equals(prefix) && "urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2".equals(namespaceURI)) {
			return;
		}
		super.writeNamespace(prefix, namespaceURI);
	}

}
