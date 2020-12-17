package dk.erst.cm.xml.syntax;

import javax.xml.bind.UnmarshallerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * Some Peppol-related structures (e.g. EHF 3.0) are described in Syntax instead of Structure tags and namespaces.
 * 
 * This handler hides the difference and produces same model for both
 */
public class StructureHandler extends XMLFilterImpl {

	private Object result;

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();

		try {
			this.result = ((UnmarshallerHandler) this.getContentHandler()).getResult();
		} catch (Exception e) {
			throw new SAXException("Failed to unmarshall result at document end", e);
		}
	}

	public final Object getResult() {
		return result;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		localName = fixSyntaxLocalName(localName);
		qName = fixSyntaxQName(qName);
		uri = fixSyntaxURI(uri);
		super.startElement(uri, localName, qName, atts);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		localName = fixSyntaxLocalName(localName);
		qName = fixSyntaxQName(qName);
		uri = fixSyntaxURI(uri);
		super.endElement(uri, localName, qName);
	}

	public String fixSyntaxLocalName(String localName) {
		localName = fixSyntaxQName(localName);
		return localName;
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		uri = fixSyntaxURI(uri);
		super.startPrefixMapping(prefix, uri);
	}

	private String fixSyntaxQName(String qName) {
		if (qName.equals("Syntax")) {
			qName = "Structure";
		}
		return qName;
	}

	private String fixSyntaxURI(String uri) {
		if (uri.equals("urn:fdc:difi.no:2017:vefa:structure:Syntax-1")) {
			uri = "urn:fdc:difi.no:2017:vefa:structure-1";
		}
		return uri;
	}
}
