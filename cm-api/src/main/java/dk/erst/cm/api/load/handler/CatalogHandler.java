package dk.erst.cm.api.load.handler;

import java.util.Enumeration;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

import lombok.Getter;
import lombok.Setter;

/**
 * Based on example from https://github.com/javaee/jaxb-v2/blob/master/jaxb-ri/samples/src/main/samples/partial-unmarshalling/src/Splitter.java
 */
public class CatalogHandler<H, L> extends XMLFilterImpl {

	private UnmarshallerHandler unmarshallerHandler;

	private Unmarshaller headUnmarshaller;
	private Unmarshaller lineUnmarshaller;
	private DefaultHandler defaultHandler;

	@Getter
	@Setter
	private boolean loadLines = true;

	@Getter
	@Setter
	private CatalogConsumer<H, L> consumer;

	public static class StopParseException extends SAXException {

		private static final long serialVersionUID = 5967628673896950778L;

	}

	public CatalogHandler(Unmarshaller headUnmarshaller, Unmarshaller lineUnmarshaller) throws JAXBException {
		this.headUnmarshaller = headUnmarshaller;
		this.lineUnmarshaller = lineUnmarshaller;
		this.defaultHandler = new DefaultHandler();
	}

	/**
	 * Keeps a reference to the locator object so that we can later pass it to a JAXB unmarshaller.
	 */
	private Locator locator;

	@Override
	public void setDocumentLocator(Locator locator) {
		super.setDocumentLocator(locator);
		this.locator = locator;
	}

	/**
	 * Used to keep track of in-scope namespace bindings.
	 * 
	 * For JAXB unmarshaller to correctly unmarshal documents, it needs to know all the effective namespace declarations.
	 */
	private NamespaceSupport namespaces = new NamespaceSupport();

	private boolean parseHead;

	public void startPrefixMapping(String prefix, String uri) throws SAXException {
		namespaces.pushContext();
		namespaces.declarePrefix(prefix, uri);

		super.startPrefixMapping(prefix, uri);
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		namespaces.popContext();

		super.endPrefixMapping(prefix);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("Catalogue")) {
			unmarshallerHandler = headUnmarshaller.getUnmarshallerHandler();
			parseHead = true;
			prepareUnmarshaller();

			super.startElement(namespaceURI, localName, qName, atts);
		} else if (localName.equals("CatalogueLine")) {
			if (parseHead) {
				endPrefixes();
				super.endElement("urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2", "Catalogue", "Catalogue");
				unmarshallerHandler.endDocument();

				try {
					consumer.consumeHead((H) unmarshallerHandler.getResult());
				} catch (JAXBException je) {
					throw new SAXException("Failed to unmarshall Catalogue on line " + locator.getLineNumber(), je);
				}

				setContentHandler(defaultHandler);

				unmarshallerHandler = null;
				if (!loadLines) {
					throw new StopParseException();
				}
				parseHead = false;
			}

			unmarshallerHandler = lineUnmarshaller.getUnmarshallerHandler();
			prepareUnmarshaller();

			super.startElement(namespaceURI, localName, qName, atts);
		} else {
			super.startElement(namespaceURI, localName, qName, atts);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		super.endElement(namespaceURI, localName, qName);

		if (localName.equals("CatalogueLine")) {
			endPrefixes();

			unmarshallerHandler.endDocument();
			try {
				consumer.consumeLine((L) unmarshallerHandler.getResult());
			} catch (JAXBException je) {
				throw new SAXException("Failed to unmarshall CatalogueLine on line " + locator.getLineNumber(), je);
			}

			setContentHandler(defaultHandler);

			unmarshallerHandler = null;

			if (!loadLines) {
				throw new StopParseException();
			}

		}
	}

	private void prepareUnmarshaller() throws SAXException {
		setContentHandler(unmarshallerHandler);
		unmarshallerHandler.startDocument();
		unmarshallerHandler.setDocumentLocator(locator);

		startPrefixes();
	}

	private void startPrefixes() throws SAXException {
		Enumeration<?> e = namespaces.getPrefixes();
		while (e.hasMoreElements()) {
			String prefix = (String) e.nextElement();
			String uri = namespaces.getURI(prefix);

			unmarshallerHandler.startPrefixMapping(prefix, uri);
		}
		String defaultURI = namespaces.getURI("");
		if (defaultURI != null) {
			unmarshallerHandler.startPrefixMapping("", defaultURI);
		}
	}

	private void endPrefixes() throws SAXException {
		Enumeration<?> e = namespaces.getPrefixes();
		while (e.hasMoreElements()) {
			String prefix = (String) e.nextElement();
			unmarshallerHandler.endPrefixMapping(prefix);
		}
		String defaultURI = namespaces.getURI("");
		if (defaultURI != null) {
			unmarshallerHandler.endPrefixMapping("");
		}
	}

}
