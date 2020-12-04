package dk.erst.cm.api.load.handler;

import java.util.Enumeration;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.XMLFilterImpl;

import dk.erst.cm.api.load.PeppolLoadService.StopParseException;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import lombok.Getter;
import lombok.Setter;

/**
 * Based on example from https://github.com/javaee/jaxb-v2/blob/master/jaxb-ri/samples/src/main/samples/partial-unmarshalling/src/Splitter.java
 */
public class CatalogHandler extends XMLFilterImpl {

	private UnmarshallerHandler unmarshallerHandler;

	private Unmarshaller catalogUnmarshaller;
	private Unmarshaller catalogLineUnmarshaller;
	private DefaultHandler defaultHandler;

	@Getter
	@Setter
	private boolean loadLines = true;

	@Getter
	@Setter
	private CatalogConsumer consumer;

	public CatalogHandler() throws JAXBException {
		catalogUnmarshaller = JAXBContext.newInstance(Catalogue.class).createUnmarshaller();
		catalogLineUnmarshaller = JAXBContext.newInstance(CatalogueLine.class).createUnmarshaller();
		defaultHandler = new DefaultHandler();
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

	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
		if (localName.equals("Catalogue")) {
			unmarshallerHandler = catalogUnmarshaller.getUnmarshallerHandler();
			parseHead = true;
			prepareUnmarshaller();

			super.startElement(namespaceURI, localName, qName, atts);
		} else if (localName.equals("CatalogueLine")) {
			if (parseHead) {
				endPrefixes();
				super.endElement("urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2", "Catalogue", "Catalogue");
				unmarshallerHandler.endDocument();

				try {
					consumer.consumeHead((Catalogue) unmarshallerHandler.getResult());
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

			unmarshallerHandler = catalogLineUnmarshaller.getUnmarshallerHandler();
			prepareUnmarshaller();

			super.startElement(namespaceURI, localName, qName, atts);
		} else {
			super.startElement(namespaceURI, localName, qName, atts);
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

		// forward this event
		super.endElement(namespaceURI, localName, qName);

		if (localName.equals("CatalogueLine")) {
			// just finished sending one chunk.
			endPrefixes();

			unmarshallerHandler.endDocument();

			// then retrieve the fully unmarshalled object
			try {
				CatalogueLine line = (CatalogueLine) unmarshallerHandler.getResult();
				consumer.consumeLine(line);
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
