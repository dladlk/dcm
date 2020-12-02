package dk.erst.cm.api.load.handler;

import java.util.ArrayList;
import java.util.List;

import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;
import lombok.Getter;

@Getter
public class ListCatalogConsumer implements CatalogConsumer {

	private Catalogue catalog;
	private List<CatalogueLine> list;

	public ListCatalogConsumer() {
		list = new ArrayList<CatalogueLine>();
	}

	@Override
	public void consumeHead(Catalogue catalog) {
		this.catalog = catalog;
	}

	@Override
	public void consumeLine(CatalogueLine line) {
		list.add(line);
	}

}