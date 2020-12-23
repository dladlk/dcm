package dk.erst.cm.webapi;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.api.item.LoadCatalogService;
import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploadConsumer implements CatalogConsumer<Catalogue, CatalogueLine> {

	private LoadCatalogService loadCatalogService;

	@Getter
	private ProductCatalogUpdate productCatalogUpdate;

	@Getter
	private int lineCount;

	@Getter
	private Map<LineAction, Integer> lineActionStat;

	public static enum LineAction {
		ADD, UPDATE, DELETE
	}

	public FileUploadConsumer(LoadCatalogService loadCatalogService) {
		this.loadCatalogService = loadCatalogService;
		this.lineActionStat = new TreeMap<FileUploadConsumer.LineAction, Integer>();
		Arrays.stream(LineAction.values()).forEach(la -> this.lineActionStat.put(la, 0));
	}

	@Override
	public void consumeHead(Catalogue catalog) {
		this.productCatalogUpdate = loadCatalogService.saveCatalogue(catalog);
	}

	@Override
	public void consumeLine(CatalogueLine line) {
		Product product = this.loadCatalogService.saveCatalogUpdateItem(productCatalogUpdate, line);
		LineAction action = product == null ? LineAction.DELETE : product.getVersion() == 1 ? LineAction.ADD : LineAction.UPDATE;

		this.lineCount++;
		this.lineActionStat.put(action, this.lineActionStat.get(action) + 1);

		if (this.lineCount % 100 == 0) {
			log.info(String.format("Loaded %d lines, stat: ", this.lineCount, this.lineActionStat.toString()));
		}
	}
}
