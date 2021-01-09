package dk.erst.cm.xml.syntax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import dk.erst.cm.xml.syntax.codelist.CodeList;
import dk.erst.cm.xml.syntax.structure.AttributeType;
import dk.erst.cm.xml.syntax.structure.ElementType;
import dk.erst.cm.xml.syntax.structure.ReferenceType;
import dk.erst.cm.xml.syntax.structure.StructureType;
import dk.erst.cm.xml.syntax.structure.ValueEnum;
import dk.erst.cm.xml.syntax.structure.ValueType;
import lombok.Getter;
import lombok.Setter;

public class JsonSchemaLoadServiceTest {
	
	private static final int ENUM_LIMIT = 5000;

	@Test
	public void testGenerateTxtSyntax() throws IOException {
		File outputFolder = new File("/wsid/xml-builder/src/schemas");
		if (!outputFolder.exists()) {
			System.out.println("Skipping schemas generation, as folder " + outputFolder.getCanonicalPath() + " does not exist");
			return;
		}
		StructureLoadService service = new StructureLoadService();
		String rootPath = StructureLoadService.DEFAULT_SYNTAX_PATH;

		String[][] structures = new String[][] {

				new String[] { "ubl-catalogue.xml", "Peppol BIS3 Catalogue" },

				new String[] { "ehf-3.0/ehf-catalogue-3.0.xml", "EHF Catalogue 3.0" },

				new String[] { "ehf-1.0/catalogue.xml", "EHF Catalogue 1.0" },

				new String[] { "ubl-order.xml", "Peppol BIS3 Order" },

				new String[] { "ehf-3.0/ehf-order-3.0.xml", "EHF Order 3.0" },

				new String[] { "ehf-1.0/order.xml", "EHF Order 1.0" },

				new String[] { "ubl-order-response.xml", "Peppol BIS3 OrderResponse" },

				new String[] { "ehf-3.0/ehf-order-response-3.0.xml", "EHF OrderResponse 3.0" },

				new String[] { "ehf-1.0/order-response.xml", "EHF OrderResponse 1.0" },

		};

		JsonSchemaDumpService sd = new JsonSchemaDumpService();
		sd.setRemoveTagNsAlias(true);
		for (String[] strings : structures) {
			String name = strings[1];
			String pathname = rootPath + "/" + strings[0];

			System.out.println("Generate schema for " + pathname);

			StructureType s;
			try (InputStream is = new FileInputStream(new File(pathname))) {
				s = service.loadStructure(is, pathname);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
			}
			JsonSchema ds = sd.dump(s, name);

			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Include.NON_NULL);
			String dss = om.writerWithDefaultPrettyPrinter().writeValueAsString(ds);

			StringBuilder sb = new StringBuilder();
			sb.append("const xmlSchema = ");
			sb.append(dss);
			sb.append("; export default xmlSchema;");

			String fileName = name.replace(" ", "_") + ".js";
			Path resultPath = new File(outputFolder, fileName).toPath();
			Files.write(resultPath, sb.toString().getBytes(StandardCharsets.UTF_8));

			System.out.println("Done " + resultPath);
		}
	}

	public static enum JsonSchemaType {
		object, string, number, integer, array,
	}

	@Getter
	@Setter
	public static class JsonSchema {
		private String title;
		private JsonSchemaType type;
		private List<String> required;
		private Map<String, JsonSubSchema> properties;
	}

	@Getter
	@Setter
	private static class JsonSubSchema {
		private JsonSchemaType type;
		private String title;
		@JsonProperty("default")
		private String defaultValue;
		private String description;
		private Map<String, JsonSubSchema> properties;
		private JsonSubSchema items;
		@JsonProperty("enum")
		public List<String> enumList;

		private Integer minItems;
		private Integer maxItems;
	}

	private static class JsonSchemaDumpService {
		@Getter
		@Setter
		private boolean removeTagNsAlias = false;

		private CodeListLoadService service = new CodeListLoadService();

		private Map<CodeListStandard, CodeList> codeListMap = new HashMap<CodeListStandard, CodeList>();

		public JsonSchema dump(StructureType s, String name) {
			JsonSchema j = new JsonSchema();
			j.setTitle(name);
			j.setType(JsonSchemaType.object);
			Map<String, JsonSubSchema> map = new LinkedHashMap<String, JsonSchemaLoadServiceTest.JsonSubSchema>();
			j.setProperties(map);
			this.dump(map, s.getDocument(), 0);
			return j;
		}

		private JsonSubSchema dumpLine(Map<String, JsonSubSchema> map, int level, String term, String name, String description, ValueType v, List<ReferenceType> referenceList) {
			if (level >= 1) {
				JsonSubSchema js = new JsonSubSchema();
				if (removeTagNsAlias) {
					int colonIndex = term.indexOf(":");
					if (colonIndex > 0) {
						term = term.substring(colonIndex + 1);
					}
				}
				js.setTitle(term + (name != null ? ": " + name : ""));
				js.setType(JsonSchemaType.string);
				if (description != null) {
					description = description.replaceAll("\\n\\s{3,}", " ");
				}
				js.setDescription(description);

				String fieldId = term;
				if (fieldId.length() > 2) {
					fieldId = Character.toLowerCase(term.charAt(0)) + term.substring(1);
				} else {
					fieldId = fieldId.toLowerCase();
				}

				if (v != null) {
					js.setDefaultValue(v.getValue());
					if (v.getType() == ValueEnum.FIXED) {
						js.setDescription(js.getDescription() + " FIXED VALUE " + v.getValue());
					}
				}

				if (referenceList != null && !referenceList.isEmpty()) {
					for (ReferenceType reference : referenceList) {
						String referenceType = reference.getType();
						String referenceValue = reference.getValue();
						if ("BUSINESS_TERM".equals(referenceType)) {
							addBusinessTerm(js, referenceValue);
						} else if ("CODE_LIST".equals(referenceType)) {
							CodeList codeList = getCodeList(referenceValue);
							/*
							 * Very strange - but there are 2 values TSP in ItemClassificationCode UNCL7143 @listId
							 */
							js.enumList = codeList.getCode().stream().map(c -> c.getId()).collect(Collectors.toList());

							Set<String> codeSet = new HashSet<String>();
							for (String code : js.enumList) {
								if (!codeSet.add(code)) {
									System.out.println("WARNING: Code list " + referenceValue + " has duplicated value " + code);
								}
							}

							js.enumList = js.enumList.stream().distinct().collect(Collectors.toList());
							if (js.enumList.size() > ENUM_LIMIT) {
								System.out.println("WARNING: Code list " + referenceValue + " has more than " + ENUM_LIMIT + " values: " + js.enumList.size() + ", limit it to first " + ENUM_LIMIT);
								js.enumList = js.enumList.subList(0, ENUM_LIMIT);
							}
						}

					}
				}

				map.put(fieldId, js);
				return js;
			}
			return null;
		}

		private CodeList getCodeList(String codeList) {
			CodeListStandard codeListStandard = CodeListStandard.getInstanceByCode(codeList);
			if (codeListStandard == null) {
				throw new IllegalStateException("Unknown code list " + codeList);
			}
			CodeList codeListData = codeListMap.get(codeListStandard);
			if (codeListData == null) {
				codeListData = service.loadCodeList(codeListStandard);
				codeListMap.put(codeListStandard, codeListData);
			}
			return codeListData;
		}

		private void addBusinessTerm(JsonSubSchema js, String value) {
			if (js.getDescription() != null) {
				StringBuilder sb = new StringBuilder();
				if (!js.getDescription().endsWith(".")) {
					sb.append(". ");
				}
				sb.append("Business term ");
				sb.append(value);
				sb.append(".");
				js.setDescription(js.getDescription() + sb.toString());
			}
		}

		private void dump(Map<String, JsonSubSchema> map, ElementType s, int level) {
			String termValue = s.getTerm().getValue();
			JsonSubSchema newElement = dumpLine(map, level, termValue, s.getName() != null ? s.getName().getValue() : null, s.getDescription(), s.getValue(), s.getReference());

			List<AttributeType> attributeList = s.getAttribute();
			List<Object> list = s.getElementOrInclude();

			if (attributeList.isEmpty() && list.isEmpty()) {
				return;
			}

			if (level > 0) {
				newElement.setType(JsonSchemaType.array);

				if ("0..1".equals(s.getCardinality())) {
					newElement.setMinItems(0);
					newElement.setMaxItems(1);
				} else if (s.getCardinality() == null || "1..1".equals(s.getCardinality())) {
					newElement.setMinItems(1);
					newElement.setMaxItems(1);
				} else if ("1..n".equals(s.getCardinality())) {
					newElement.setMinItems(1);
				}

				JsonSubSchema items = new JsonSubSchema();
				items.setType(JsonSchemaType.object);
				map = new LinkedHashMap<String, JsonSchemaLoadServiceTest.JsonSubSchema>();
				items.setProperties(map);
				newElement.setItems(items);
			}

			if (!attributeList.isEmpty()) {
				dumpLine(map, level + 1, "value", "Value", s.getDescription(), s.getValue(), s.getReference());
			}

			for (AttributeType sa : attributeList) {
				// String cardinality = sa.getUsage() == null ? "M" : sa.getUsage().substring(0, 1);
				dumpLine(map, level + 1, "@" + sa.getTerm().getValue(), sa.getName() != null ? sa.getName().getValue() : null, sa.getDescription(), sa.getValue(), sa.getReference());
			}

			for (Object elementOrInclude : list) {
				if (elementOrInclude instanceof ElementType) {
					dump(map, (ElementType) elementOrInclude, level + 1);
				} else {
					throw new UnsupportedOperationException("Found IncludeType which is not yet supported: " + elementOrInclude);
				}
			}
		}

	}

}
