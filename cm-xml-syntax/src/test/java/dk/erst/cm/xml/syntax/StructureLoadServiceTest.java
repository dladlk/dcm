package dk.erst.cm.xml.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
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
	public void testSyntax() throws IOException {
		StructureLoadService service = new StructureLoadService();
		StructureType s = service.loadPeppolCatalogueStructure();
		assertNotNull(s);

		StructureDumpService sd = new StructureDumpService();
		sd.setRemoveTagNsAlias(true);
		String dumpedStructure = sd.dump(s);

		List<String> actualLines = Arrays.asList(dumpedStructure.split(System.lineSeparator()));
		File compareWithFile = new File("../../peppol-oioubl-convertion/xml-resources/examples/Tree_Catalogue_BIS3.txt");
		if (compareWithFile.exists()){
			List<String> expectedLines = Files.readAllLines(compareWithFile.toPath(), StandardCharsets.UTF_8);
			
			for (int i = 0; i < expectedLines.size(); i++) {
				String expectedLine = expectedLines.get(i).trim();
				String actualLine = actualLines.get(i);
				System.out.println(expectedLine);
				assertEquals(expectedLine, actualLine, "Line " + i);
			}
		} else {
			System.out.println("File "+compareWithFile.getCanonicalPath()+" is not found, skip comparing");
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
