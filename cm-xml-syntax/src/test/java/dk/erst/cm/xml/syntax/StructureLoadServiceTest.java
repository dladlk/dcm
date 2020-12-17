package dk.erst.cm.xml.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import dk.erst.cm.xml.syntax.structure.AttributeType;
import dk.erst.cm.xml.syntax.structure.ElementType;
import dk.erst.cm.xml.syntax.structure.StructureType;
import lombok.Getter;
import lombok.Setter;

public class StructureLoadServiceTest {

	@Test
	public void testGenerateTxtSyntax() throws IOException {
		File outputFolder = new File("/temp/structure");
		if (!outputFolder.exists()) {
			System.out.println("Skipping structure generation, as folder " + outputFolder.getCanonicalPath() + " does not exist");
			return;
		}
		StructureLoadService service = new StructureLoadService();
		String rootPath = StructureLoadService.DEFAULT_SYNTAX_PATH;

		String[][] structures = new String[][] { new String[] { "ubl-catalogue.xml", "Tree_Catalogue_BIS3.txt" },

				new String[] { "ehf-3.0/ehf-catalogue-3.0.xml", "Tree_Catalogue_EHF3.txt" },

				new String[] { "ehf-1.0/catalogue.xml", "Tree_Catalogue_EHF1.txt" },

				new String[] { "ubl-order.xml", "Tree_Order_BIS3.txt" },

				new String[] { "ehf-3.0/ehf-order-3.0.xml", "Tree_Order_EHF3.txt" },

				new String[] { "ehf-1.0/order.xml", "Tree_Order_EHF1.txt" },

				new String[] { "ubl-order-response.xml", "Tree_OrderResponse_BIS3.txt" },

				new String[] { "ehf-3.0/ehf-order-response-3.0.xml", "Tree_OrderResponse_EHF3.txt" },

				new String[] { "ehf-1.0/order-response.xml", "Tree_OrderResponse_EHF1.txt" },

		};

		StructureDumpService sd = new StructureDumpService();
		sd.setRemoveTagNsAlias(true);
		for (String[] strings : structures) {
			String pathname = rootPath + "/" + strings[0];
			StructureType s;
			try (InputStream is = new FileInputStream(new File(pathname))) {
				s = service.loadStructure(is, pathname);
			} catch (Exception e) {
				throw new IllegalStateException("Failed to load Peppol Catalogue structure by path " + pathname, e);
			}
			String ds = sd.dump(s);
			Files.write(new File(outputFolder, strings[1]).toPath(), ds.getBytes(StandardCharsets.UTF_8));
		}
	}

	@Test
	public void testSyntax() throws IOException {
		StructureLoadService service = new StructureLoadService();
		StructureType s = service.loadPeppolCatalogueStructure();
		assertNotNull(s);

		StructureDumpService sd = new StructureDumpService();
		sd.setRemoveTagNsAlias(true);
		String dumpedStructure = sd.dump(s);

		List<String> actualLines = Arrays.asList(dumpedStructure.split(System.lineSeparator()));
		File compareWithFile = new File("../../peppol-oioubl-convertion/xml-resources/examples/Tree_Catalogue_BIS3.txt");
		if (compareWithFile.exists()) {
			List<String> expectedLines = Files.readAllLines(compareWithFile.toPath(), StandardCharsets.UTF_8);

			for (int i = 0; i < expectedLines.size(); i++) {
				String expectedLine = expectedLines.get(i).trim();
				String actualLine = actualLines.get(i);
				System.out.println(expectedLine);
				assertEquals(expectedLine, actualLine, "Line " + i);
			}
		} else {
			System.out.println("File " + compareWithFile.getCanonicalPath() + " is not found, skip comparing");
		}
	}

	private static class StructureDumpService {
		@Getter
		@Setter
		private boolean removeTagNsAlias = false;

		public String dump(StructureType s) {
			StringBuilder sb = new StringBuilder();
			this.dump(sb, s.getDocument(), 0);
			return sb.toString();
		}

		private void dumpLine(StringBuilder sb, int level, String term, String cardinality) {
			if (level >= 1) {
				String cardinalityValue = cardinality == null ? "1..1" : cardinality;
				sb.append(cardinalityValue);
				sb.append(" 	");
				sb.append(prefix(level));
				if (removeTagNsAlias) {
					int colonIndex = term.indexOf(":");
					if (colonIndex > 0) {
						term = term.substring(colonIndex + 1);
					}
				}
				sb.append(term);
				sb.append(System.lineSeparator());

			}
		}

		private void dump(StringBuilder sb, ElementType s, int level) {
			dumpLine(sb, level, s.getTerm().getValue(), s.getCardinality());
			List<AttributeType> attributeList = s.getAttribute();
			for (AttributeType sa : attributeList) {
				String cardinality = sa.getUsage() == null ? "M" : sa.getUsage().substring(0, 1);
				dumpLine(sb, level + 1, "@" + sa.getTerm().getValue(), cardinality);
			}
			List<Object> list = s.getElementOrInclude();
			for (Object elementOrInclude : list) {
				if (elementOrInclude instanceof ElementType) {
					dump(sb, (ElementType) elementOrInclude, level + 1);
				} else {
					throw new UnsupportedOperationException("Found IncludeType which is not yet supported: " + elementOrInclude);
				}
			}
		}

		private String prefix(int level) {
			return String.join("", Collections.nCopies(level, "•   "));
		}
	}

}
