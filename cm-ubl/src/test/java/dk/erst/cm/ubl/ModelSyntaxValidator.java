package dk.erst.cm.ubl;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;

import org.junit.jupiter.api.Test;

import dk.erst.cm.xml.syntax.StructureLoadService;
import dk.erst.cm.xml.syntax.structure.ElementType;
import dk.erst.cm.xml.syntax.structure.NamespaceType;
import dk.erst.cm.xml.syntax.structure.StructureType;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.annotations.Mandatory;

class ModelSyntaxValidator {

	@Test
	void test() throws JAXBException, FileNotFoundException, IOException {
		StructureLoadService structureLoadService = new StructureLoadService();
		String pathname = "../cm-resources/structure/syntax/ubl-catalogue.xml";
		StructureType s = null;
		try (InputStream is = new FileInputStream(new File(pathname))) {
			s = structureLoadService.loadStructure(is, pathname);
		}

		Map<String, String> namespaceToPrefixMap = s.getNamespace().stream().collect(Collectors.toMap(NamespaceType::getValue, NamespaceType::getPrefix));
		validateStructure(Catalogue.class, s.getDocument(), namespaceToPrefixMap);
	}

	private void validateStructure(Class<?> c, ElementType element, Map<String, String> namespaceToPrefixMap) {
		Field[] classFields = c.getDeclaredFields();
		Map<String, Field> xmlElementNameToFieldMap = new HashMap<String, Field>();
		for (Field field : classFields) {
			XmlElement xmlElementAnnotation = field.getAnnotation(XmlElement.class);
			if (xmlElementAnnotation != null) {
				String name = xmlElementAnnotation.name();
				String namespace = xmlElementAnnotation.namespace();
				String prefix = namespaceToPrefixMap.get(namespace);
				if (prefix == null) {
					throw new IllegalArgumentException("Unsupported namespace, not described in Structure document: " + namespace);
				}
				String xmlElementName = prefix + ":" + name;
				xmlElementNameToFieldMap.put(xmlElementName, field);
			}
		}

		for (Object elementOrInclude : element.getElementOrInclude()) {
			if (elementOrInclude instanceof ElementType) {
				ElementType el = (ElementType) elementOrInclude;

				String name = el.getTerm().getValue();
				boolean mandatory = el.getCardinality() == null || el.getCardinality().startsWith("1..");

				System.out.println(String.join("\t", mandatory ? "M" : "O", name));

				Field modelField = xmlElementNameToFieldMap.get(name);
				assertFalse("Model misses tag " + name, modelField == null);

				boolean mandatoryModel = modelField.getAnnotationsByType(Mandatory.class).length > 0;

				assertEquals(mandatory, mandatoryModel, "Field " + modelField.getName() + "with tag name " + name + " has different mandatory mark in syntax and model");

			} else {
				throw new IllegalArgumentException("Include is not supported yet: found for " + elementOrInclude);
			}
		}
	}

}
