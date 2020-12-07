package dk.erst.cm.api.load;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dk.erst.cm.xml.ubl21.model.Catalogue;

class MapperServiceTest {

	@Test
	void testConvert() {
		MapperService s = new MapperService();
		Catalogue c21 = new Catalogue();
		c21.setActionCode("TEST");
		dk.erst.cm.xml.ubl20.model.Catalogue c20 = s.convert(c21);
		assertEquals(c20.getActionCode(), c21.getActionCode());
	}

}
