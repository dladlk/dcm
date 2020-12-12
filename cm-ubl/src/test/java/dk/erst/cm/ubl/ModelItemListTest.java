package dk.erst.cm.ubl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.Test;

import dk.erst.cm.xml.syntax.StructureLoadService;
import dk.erst.cm.xml.syntax.structure.AttributeType;
import dk.erst.cm.xml.syntax.structure.ElementType;
import dk.erst.cm.xml.syntax.structure.SomeType;
import dk.erst.cm.xml.syntax.structure.StructureType;

public class ModelItemListTest {

	@Test
	public void test() throws FileNotFoundException, IOException, JAXBException {
		StructureLoadService structureLoadService = new StructureLoadService();
		StructureType structure = structureLoadService.loadPeppolCatalogueStructure();
		Optional<Object> catalogueLine = findChild(structure, "cac:CatalogueLine");
		assertTrue(catalogueLine.isPresent());

		list((ElementType) catalogueLine.get(), 0);
	}

	private void dump(String mandatory, String multiple, int level, String name, String description) {
		System.out.println(String.join("", mandatory, multiple, "\t", levelPrefix(level), name, "\t\t\t" + cleanText(description)));
	}

	private String cleanText(String s) {
		if (s == null) {
			return "";
		}
		return s.replaceAll("\\s+", " ");
	}

	private void list(ElementType element, int level) {
		{
			String name = element.getTerm().getValue();
			boolean mandatory = element.getCardinality() == null || element.getCardinality().startsWith("1..");
			boolean multiple = element.getCardinality() != null && element.getCardinality().endsWith("..n");
			String description = getDescription(element);
			dump(mandatory ? "M" : "O", multiple ? "N" : "1", level, name, description);
		}
		if (element.getAttribute().size() > 0) {
			List<AttributeType> attributeList = element.getAttribute();
			for (AttributeType attribute : attributeList) {
				String name = "@" + attribute.getTerm().getValue();
				String description = getDescription(attribute);
				boolean mandatory = attribute.getUsage() == null ? true : "M".equals(attribute.getUsage().substring(0, 1));
				dump(mandatory ? "M" : "O", "@", level + 1, name, description);
			}
		}

		for (Object elementOrInclude : element.getElementOrInclude()) {
			if (elementOrInclude instanceof ElementType) {
				ElementType el = (ElementType) elementOrInclude;
				list(el, level + 1);
			}
		}

	}

	private String getDescription(SomeType attribute) {
		return attribute.getName() != null ? attribute.getName().getValue() : null;
	}

	private Optional<Object> findChild(StructureType structure, String tagName) {
		return structure.getDocument().getElementOrInclude().stream().filter(s -> ((ElementType) s).getTerm().getValue().equals(tagName)).findFirst();
	}

	private String levelPrefix(int level) {
		return String.join("", Collections.nCopies(level, "- "));
	}

}
