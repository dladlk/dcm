package dk.erst.cm.api.load.handler;

import java.util.Iterator;

public interface CatalogProducer<H, L> {

	public H produceHead();

	public Iterator<L> lineIterator();

}
