package dk.erst.cm.api.load.handler;

import dk.erst.cm.api.load.model.Catalogue;
import dk.erst.cm.api.load.model.CatalogueLine;

public interface CatalogConsumer {

	public void consumeHead(Catalogue catalog);

	public void consumeLine(CatalogueLine line);

}
