package dk.erst.cm.api.load.handler;

public interface CatalogConsumer<H, L> {

	public void consumeHead(H catalog);

	public void consumeLine(L line);

}
