package dk.erst.cm.xml.syntax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import dk.erst.cm.xml.syntax.structure.ObjectFactory;
import dk.erst.cm.xml.syntax.structure.StructureType;

public class StructureLoadService {

	public static final String DEFAULT_SYNTAX_PATH = "../cm-resources/structure/syntax"; 
	
	public StructureType loadStructure(InputStream is, String description) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(true);
		try {
			XMLReader reader = factory.newSAXParser().getXMLReader();
			
			StructureHandler structureHandler = new StructureHandler();
			structureHandler.setContentHandler(unmarshallerHandler);
			reader.setContentHandler(structureHandler);
			
			reader.parse(new InputSource(is));
			
			@SuppressWarnings("unchecked")
			JAXBElement<StructureType> result = (JAXBElement<StructureType>) structureHandler.getResult();
			
			return result.getValue();
		} catch (Exception e) {
			throw new JAXBException("", e);
		}
	}

	public StructureType loadPeppolCatalogueStructure() {
		String pathname = DEFAULT_SYNTAX_PATH + "/ubl-catalogue.xml";
		try (InputStream is = new FileInputStream(new File(pathname))) {
			return this.loadStructure(is, pathname);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
		}
	}
	
	public StructureType loadEHF3CatalogueStructure() {
		String pathname = DEFAULT_SYNTAX_PATH + "/ehf-3.0/ehf-catalogue-3.0.xml";
		try (InputStream is = new FileInputStream(new File(pathname))) {
			return this.loadStructure(is, pathname);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
		}
	}
	
	public StructureType loadEHF1CatalogueStructure() {
		String pathname = DEFAULT_SYNTAX_PATH + "/ehf-1.0/catalogue.xml";
		try (InputStream is = new FileInputStream(new File(pathname))) {
			return this.loadStructure(is, pathname);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
		}
	}
	
}
