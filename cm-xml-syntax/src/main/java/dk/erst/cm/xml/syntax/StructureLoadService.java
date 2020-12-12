package dk.erst.cm.xml.syntax;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import dk.erst.cm.xml.syntax.structure.ObjectFactory;
import dk.erst.cm.xml.syntax.structure.StructureType;

public class StructureLoadService {

	public StructureType parseStructure(InputStream is, String description) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<StructureType> o = (JAXBElement<StructureType>) unmarshaller.unmarshal(is);
		return o.getValue();
	}

}
