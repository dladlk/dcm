package dk.erst.cm.xml.syntax;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import dk.erst.cm.xml.syntax.codelist.CodeList;
import dk.erst.cm.xml.syntax.codelist.CodeList.Code;

public class CodeListLoadServiceTest {

	@Test
	public void testSyntax() throws IOException {
		CodeListLoadService service = new CodeListLoadService();

		CodeListStandard[] values = CodeListStandard.values();
		
		System.out.println("Found "+values.length+" code lists:");
		for (CodeListStandard codeListStandard : values) {
			CodeList s = service.loadCodeList(codeListStandard);
			assertNotNull(s);
			System.out.println("#" + s.getIdentifier() + " \"" + s.getTitle() + "\" (agency " + s.getAgency() + "), size: " + s.getCode().size());

			List<Code> codeList = s.getCode();
			assertNotNull(codeList);
			assertFalse(codeList.isEmpty());
			System.out.println(" - " + describeCode(codeList.get(0)));
			System.out.println(" - " + describeCode(codeList.get(codeList.size() - 1)));
		}
	}

	private String describeCode(Code code) {
		StringBuilder sb = new StringBuilder();
		sb.append(code.getId());
		sb.append(" - ");
		sb.append(code.getName());
		if (code.getDescription() != null) {
			sb.append(": ");
			sb.append(code.getDescription().replaceAll("\\s+", " "));
		}
		return sb.toString();
	}

}
