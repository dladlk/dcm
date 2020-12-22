package dk.erst.cm.api.build;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import dk.erst.cm.api.convert.EmptyConverter;
import dk.erst.cm.api.convert.SerializerCatalogConsumer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.ListID;

class CatalogBuilderTest {

	@Test
	void testGenerateCatalog() throws JAXBException, XMLStreamException, FactoryConfigurationError {
		Catalogue c = new Catalogue();
		c.setActionCode("Update");
		CatalogueLine cl = new CatalogueLine();
		cl.setActionCode(ListID.builder().id("Update").build());

		SerializerCatalogConsumer serializer = new SerializerCatalogConsumer(new EmptyConverter());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		serializer.start(baos);
		serializer.consumeHead(c);
		serializer.consumeLine(cl);
		serializer.finish();

		System.out.println(new String(baos.toByteArray(), StandardCharsets.UTF_8));
	}

}
