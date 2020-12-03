package dk.erst.cm.api.item;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dk.erst.cm.api.dao.mongo.ItemRepository;
import dk.erst.cm.api.data.Item;
import dk.erst.cm.api.load.PeppolLoadService;
import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;
import dk.erst.cm.test.TestDocument;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class ItemServiceTest {

	@Autowired
	private ItemRepository itemRepository;

	@Test
	void testSaveCatalogUpdateItem() throws Exception {
		ItemService itemService = new ItemService(this.itemRepository);
		PeppolLoadService peppolLoadService = new PeppolLoadService();

		try (InputStream inputStream = TestDocument.CATALOGUE_PEPPOL.getInputStream()) {
			peppolLoadService.loadXml(inputStream, "Test file", new CatalogConsumer() {
				private Catalogue catalogue;
				@Override
				public void consumeHead(Catalogue catalog) {
					this.catalogue = catalog;
				}
				@Override
				public void consumeLine(CatalogueLine line) {
					Item item = itemService.saveCatalogUpdateItem(this.catalogue, line);
					log.info("Saved item " + item.getId());
				}
			});
		}
	}

}
