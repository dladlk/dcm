package dk.erst.cm.api.load.handler;

import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public interface CatalogConsumer {

	public void consumeHead(Catalogue catalog);

	public void consumeLine(CatalogueLine line);

}
