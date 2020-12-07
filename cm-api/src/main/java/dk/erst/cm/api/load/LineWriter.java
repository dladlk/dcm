package dk.erst.cm.api.load;

import javax.xml.stream.XMLStreamWriter;

public class LineWriter extends DelegatingXMLStreamWriter {

	public LineWriter(XMLStreamWriter xmlStreamWriter) {
		super(xmlStreamWriter);
	}

}
