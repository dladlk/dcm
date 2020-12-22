package dk.erst.cm.api.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import dk.erst.cm.api.item.ProductDocumentVersion;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class Product {

	@Id
	private String id;
	private Instant createTime;
	private Instant updateTime;
	private int version;

	private String productCatalogId;
	private ProductDocumentVersion documentVersion;

	@Indexed
	private String standardNumber;

	private Object document;
}
