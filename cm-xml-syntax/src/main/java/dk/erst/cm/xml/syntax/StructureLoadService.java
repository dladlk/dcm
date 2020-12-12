package dk.erst.cm.xml.syntax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import dk.erst.cm.xml.syntax.structure.ObjectFactory;
import dk.erst.cm.xml.syntax.structure.StructureType;

public class StructureLoadService {

	public StructureType loadStructure(InputStream is, String description) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<StructureType> o = (JAXBElement<StructureType>) unmarshaller.unmarshal(is);
		return o.getValue();
	}

	public StructureType loadPeppolCatalogueStructure() {
		String pathname = "../cm-resources/structure/syntax/ubl-catalogue.xml";
		try (InputStream is = new FileInputStream(new File(pathname))) {
			return this.loadStructure(is, pathname);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
		}
	}
}
