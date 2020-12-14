package dk.erst.cm.xml.syntax;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import dk.erst.cm.xml.syntax.codelist.CodeList;
import dk.erst.cm.xml.syntax.codelist.ObjectFactory;

public class CodeListLoadService {

	public CodeList loadStructure(InputStream is, String description) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		CodeList o = (CodeList) unmarshaller.unmarshal(is);
		return o;
	}

	public CodeList loadCodeList(CodeListStandard standard) {
		String pathname = "../cm-resources/structure/codelist/" + standard.getResourceName() + ".xml";
		try (InputStream is = new FileInputStream(new File(pathname))) {
			return this.loadStructure(is, pathname);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to load code list standard " + standard + " by path " + pathname, e);
		}
	}
}
