package dk.erst.cm.api.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import dk.erst.cm.api.item.ProductDocumentVersion;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class ProductCatalogUpdate {

	@Id
	private String id;
	private Instant createTime;
	private int version;

	private ProductDocumentVersion documentVersion;
	private Object document;

	private String productCatalogId;

}
