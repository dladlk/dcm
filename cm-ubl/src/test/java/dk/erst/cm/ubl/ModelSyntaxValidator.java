package dk.erst.cm.ubl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import lombok.Data;

class ModelSyntaxValidator {

	private Map<String, String> namespaceToPrefixMap;

	private List<ModelDifference> differenceList;

	@Data
	private static class ModelDifference {
		String message;
		String modelPath;
		String xmlPath;
		String checkType;
		String expected;
		String actual;
		String field;
	}

	@Test
	void validateModel() throws JAXBException, FileNotFoundException, IOException {
		StructureLoadService structureLoadService = new StructureLoadService();
		String pathname = "../cm-resources/structure/syntax/ubl-catalogue.xml";
		StructureType s = null;
		try (InputStream is = new FileInputStream(new File(pathname))) {
			s = structureLoadService.loadStructure(is, pathname);
		}
		differenceList = new ArrayList<ModelSyntaxValidator.ModelDifference>();
		namespaceToPrefixMap = s.getNamespace().stream().collect(Collectors.toMap(NamespaceType::getValue, NamespaceType::getPrefix));
		validateStructure(Catalogue.class, s.getDocument(), 0, "", "");

		if (!this.differenceList.isEmpty()) {
			System.out.println("Found " + this.differenceList.size() + " model differences:");
			for (int i = 0; i < differenceList.size(); i++) {
				ModelDifference md = differenceList.get(i);
				System.out.println((i + 1) + ")\t" + md.getModelPath() + "." + md.getField() + "(" + md.getXmlPath() + ")" + " - " + md.getCheckType() + " should be " + md.getExpected());
			}
			assertEquals(5, this.differenceList.size());
		}
	}

	private void validateStructure(Class<?> c, ElementType element, int level, String modelPath, String xmlPath) {
		String currentModelPath = modelPath + (modelPath.length() > 0 ? "." : "") + c.getSimpleName();
		String currentXmlPath = xmlPath + (xmlPath.length() > 0 ? "/" : "") + element.getTerm().getValue();
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
				boolean multiple = el.getCardinality() != null && el.getCardinality().endsWith("..n");

				System.out.println(String.join("\t", mandatory ? "M" : "O", multiple ? "N" : "1", levelPrefix(level), name));

				Field modelField = xmlElementNameToFieldMap.get(name);
				assertFalse(modelField == null, currentModelPath + ": model misses tag " + name);

				boolean mandatoryModel = modelField.getAnnotationsByType(Mandatory.class).length > 0;
				boolean multipleModel = modelField.getType().isAssignableFrom(List.class);

				String errorPrefix = currentModelPath + ": field " + modelField.getName() + " with tag name " + name;

				assertEqualsLater(mandatory, mandatoryModel, errorPrefix + " has different mandatory mark in syntax and model", currentModelPath, currentXmlPath, "mandatory", modelField.getName());

				if (multiple && !multipleModel) {
					// As we want to support more than single model, it could be ok if model is multiple, but syntax is not.
					// But if syntax is multiple, but not model - it is an error
					assertTrue(multiple, errorPrefix + " is not marked multiple, as it is required by syntax");
				}

				if (el.getElementOrInclude().size() > 0) {
					Class<?> actualType = modelField.getType();
					if (multipleModel) {
						Type genericType = modelField.getGenericType();
						if (genericType instanceof ParameterizedType) {
							Type[] actualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
							if (actualTypeArguments.length == 1) {
								String typeName = actualTypeArguments[0].getTypeName();
								try {
									actualType = Class.forName(typeName);
								} catch (ClassNotFoundException e) {
									throw new IllegalArgumentException(errorPrefix + " defines multiple contents of class name " + typeName + " which cannot be found");
								}
							}
						}
					}
					validateStructure(actualType, el, level + 1, currentModelPath, currentXmlPath);
				}

			} else {
				throw new IllegalArgumentException("Include is not supported yet: found for " + elementOrInclude);
			}
		}
	}

	private void assertEqualsLater(boolean expected, boolean actual, String string, String currentModelPath, String currentXmlPath, String checkType, String field) {
		if (expected != actual) {
			System.out.println(string);
			ModelDifference md = new ModelDifference();
			md.setMessage(string);
			md.setModelPath(currentModelPath);
			md.setXmlPath(currentXmlPath);
			md.setField(field);
			md.setCheckType(checkType);
			md.setExpected(String.valueOf(expected));
			md.setActual(String.valueOf(actual));
			this.differenceList.add(md);
		}
	}

	private String levelPrefix(int level) {
		return String.join("", Collections.nCopies(level, "\t"));
	}

}
