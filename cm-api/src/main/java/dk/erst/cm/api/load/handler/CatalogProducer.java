package dk.erst.cm.api.load.handler;

public interface CatalogProducer<H, L> {

	public H produceHead();

	public L produceLine();

}
