package dk.erst.cm.api.load.handler;

import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

public interface CatalogProducer {

	public Catalogue produceHead();

	public CatalogueLine produceLine();

}
