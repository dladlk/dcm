package dk.erst.cm.api.load.handler;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ListCatalogConsumer<H, L> implements CatalogConsumer<H, L> {

	private H catalog;
	private List<L> list;

	public ListCatalogConsumer() {
		list = new ArrayList<L>();
	}

	@Override
	public void consumeHead(H catalog) {
		this.catalog = catalog;
	}

	@Override
	public void consumeLine(L line) {
		list.add(line);
	}

}