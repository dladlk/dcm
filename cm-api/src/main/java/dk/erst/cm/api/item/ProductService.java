package dk.erst.cm.api.item;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.dao.mongo.ProductRepository;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.NestedSchemeID;

@Service
public class ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository itemRepository) {
		this.productRepository = itemRepository;
	}

	public Product saveCatalogUpdateItem(ProductCatalogUpdate catalog, CatalogueLine line) {
		String lineLogicalId = line.getLogicalId();
		String productCatalogId = catalog.getProductCatalogId();

		String itemLogicalId = productCatalogId + "_" + lineLogicalId;

		boolean deleteAction = line.getActionCode() != null && "Delete".equals(line.getActionCode().getId());

		Product product;
		Optional<Product> optional = productRepository.findById(itemLogicalId);
		if (optional.isPresent()) {
			product = optional.get();
			product.setUpdateTime(Instant.now());
			product.setVersion(product.getVersion() + 1);
		} else {
			product = new Product();
			product.setId(itemLogicalId);
			product.setCreateTime(Instant.now());
			product.setUpdateTime(null);
			product.setVersion(1);
		}
		product.setDocumentVersion(ProductDocumentVersion.PEPPOL_CATALOGUE_3_1);
		product.setProductCatalogId(productCatalogId);
		product.setStandardNumber(getLineStandardNumber(line));
		product.setDocument(line);

		if (deleteAction) {
			if (optional.isPresent()) {
				productRepository.delete(product);
			}
			return null;
		}
		productRepository.save(product);

		return product;
	}

	private String getLineStandardNumber(CatalogueLine line) {
		if (line != null && line.getItem() != null) {
			if (line.getItem().getStandardItemIdentification() != null) {
				NestedSchemeID sn = line.getItem().getStandardItemIdentification();
				if (sn.getId() != null && sn.getId().getId() != null) {
					return sn.getId().getId().toUpperCase();
				}
			}
		}
		return null;
	}

	public long countItems() {
		return productRepository.count();
	}

	public Page<Product> findAll(String searchParam, Pageable pageable) {
		Page<Product> productList;
		if (!StringUtils.isEmpty(searchParam)) {
			TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchParam);
			productList = productRepository.findAllBy(textCriteria, pageable);
		} else {
			productList = productRepository.findAll(pageable);
		}
		return productList;
	}

	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}

	public List<Product> findByStandardNumber(String standardNumber) {
		return productRepository.findByStandardNumber(standardNumber);
	}
}
