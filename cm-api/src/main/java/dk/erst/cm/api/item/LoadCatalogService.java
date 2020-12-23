package dk.erst.cm.api.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

@Service
public class LoadCatalogService {

	private CatalogService catalogService;
	private ProductService productService;

	@Autowired
	public LoadCatalogService(CatalogService catalogService, ProductService productService) {
		this.catalogService = catalogService;
		this.productService = productService;

	}

	public ProductCatalogUpdate saveCatalogue(Catalogue catalog) {
		return catalogService.saveCatalogue(catalog);
	}

	public Product saveCatalogUpdateItem(ProductCatalogUpdate productCatalogUpdate, CatalogueLine line) {
		return this.productService.saveCatalogUpdateItem(productCatalogUpdate, line);
	}
}
