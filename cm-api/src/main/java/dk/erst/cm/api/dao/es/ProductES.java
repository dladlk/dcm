package dk.erst.cm.api.dao.es;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.Certificate;
import dk.erst.cm.xml.ubl21.model.CommodityClassification;
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

	@Field
	private String standardNumber;

	@Field
	private List<String> keywords;

	@Field
	private List<String> certificates;

	@Field
	private String origin;

	@Field
	private List<String> categories;

	public static ProductES by(Product p) {
		ProductES es = new ProductES();
		es.setId(p.getId());
		es.setProductCatalogId(p.getProductCatalogId());

		CatalogueLine cl = (CatalogueLine) p.getDocument();
		if (cl != null && cl.getItem() != null) {
			Item item = cl.getItem();

			es.setName(item.getName());
			List<String> pDescriptionList = item.getDescriptionList();
			if (isFilled(pDescriptionList)) {
				es.setDescription(pDescriptionList.stream().collect(Collectors.joining(" ")));
			}

			List<String> pKeywordList = item.getKeywordList();
			if (isFilled(pKeywordList)) {
				es.setKeywords(pKeywordList);
			}

			if (item.getStandardItemIdentification() != null) {
				es.setStandardNumber(item.getStandardItemIdentification().getId().getId());
			}
			List<Certificate> pCertificateList = item.getCertificateList();
			if (isFilled(pCertificateList)) {
				es.setCertificates(pCertificateList.stream().map(c -> c.getId() + " " + c.getCertificateType()).collect(Collectors.toList()));
			}
			if (item.getOriginCountry() != null) {
				es.setOrigin(item.getOriginCountry().getIdentificationCode());
			}

			List<CommodityClassification> pClassificationList = item.getCommodityClassificationList();
			if (isFilled(pClassificationList)) {
				es.setCategories(pClassificationList.stream().map(c -> c.getItemClassificationCode().getValue()).collect(Collectors.toList()));
			}
		}

		return es;
	}

	private static boolean isFilled(List<?> list) {
		return list != null && !list.isEmpty();
	}

}
