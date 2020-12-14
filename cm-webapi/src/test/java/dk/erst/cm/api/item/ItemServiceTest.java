package dk.erst.cm.api.item;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dk.erst.cm.api.load.PeppolLoadService;
import dk.erst.cm.test.TestDocument;
import dk.erst.cm.webapi.FileUploadConsumer;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class ItemServiceTest {

	@Autowired
	private LoadCatalogService loadCatalogService;

	@Test
	void testSaveCatalogUpdateItem() throws Exception {

		PeppolLoadService peppolLoadService = new PeppolLoadService();

		try (InputStream inputStream = TestDocument.CATALOGUE_PEPPOL.getInputStream()) {
			FileUploadConsumer consumer = new FileUploadConsumer(loadCatalogService);
			peppolLoadService.loadXml(inputStream, "Test file", consumer);
			log.info("Loaded " + consumer.getLineCount() + " lines");
		}
	}

}
