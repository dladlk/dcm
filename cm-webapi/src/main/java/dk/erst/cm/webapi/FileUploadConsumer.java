package dk.erst.cm.webapi;

import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.api.item.LoadCatalogService;
import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import lombok.Getter;

public class FileUploadConsumer implements CatalogConsumer<Catalogue, CatalogueLine> {

	private LoadCatalogService loadCatalogService;

	private ProductCatalogUpdate productCatalogUpdate;

	@Getter
	private int lineCount;

	public FileUploadConsumer(LoadCatalogService loadCatalogService) {
		this.loadCatalogService = loadCatalogService;
	}

	@Override
	public void consumeHead(Catalogue catalog) {
		this.productCatalogUpdate = loadCatalogService.saveCatalogue(catalog);
	}

	@Override
	public void consumeLine(CatalogueLine line) {
		this.loadCatalogService.saveCatalogUpdateItem(productCatalogUpdate, line);
		this.lineCount++;
	}
}
