package dk.erst.cm.api.dao.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.Item;
import lombok.Getter;
import lombok.Setter;

@Document(indexName = "product")
@Getter
@Setter
public class ProductES {

	@Id
	private String id;

	@Field(store = false)
	private String productCatalogId;

	@Field
	private String name;

	@Field
	private String description;

	public static ProductES by(Product p) {
		ProductES es = new ProductES();
		es.setId(p.getId());
		es.setProductCatalogId(p.getProductCatalogId());

		CatalogueLine cl = (CatalogueLine) p.getDocument();
		if (cl != null && cl.getItem() != null) {
			Item item = cl.getItem();

			es.setName(item.getName());
			if (item.getDescriptionList() != null && item.getDescriptionList().size() > 0) {
				es.setDescription(item.getDescriptionList().get(0));
			}
		}

		return es;
	}

}
