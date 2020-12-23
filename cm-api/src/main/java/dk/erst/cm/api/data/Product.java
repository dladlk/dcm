package dk.erst.cm.api.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import dk.erst.cm.api.item.ProductDocumentVersion;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(language = "danish")
@Data
@NoArgsConstructor
public class Product {

	public static final String[] FULL_TEXT_SEARCH_FIELDS = new String[] {

			"document.item.name",

			"document.item.descriptionList",

			"document.item.keywordList",

			"document.item.standardItemIdentification.id.id",

			"document.item.certificateList.id",

			"document.item.certificateList.certificateType",

			"document.item.originCountry.identificationCode",

			"document.item.commodityClassificationList.itemClassificationCode.value",

	};

	@Id
	private String id;
	private Instant createTime;
	private Instant updateTime;
	private int version;

	@Indexed
	private String productCatalogId;

	private ProductDocumentVersion documentVersion;

	@Indexed
	private String standardNumber;

	private Object document;
}
