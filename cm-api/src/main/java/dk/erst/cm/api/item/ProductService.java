package dk.erst.cm.api.item;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.dao.mongo.ProductRepository;
import dk.erst.cm.api.data.Product;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;
import dk.erst.cm.xml.ubl21.model.Party;
import dk.erst.cm.xml.ubl21.model.SchemeID;

@Service
public class ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductService(ProductRepository itemRepository) {
		this.productRepository = itemRepository;
	}

	public Product saveCatalogUpdateItem(Catalogue catalogue, CatalogueLine line) {
		String lineLogicalId = line.getLogicalId();
		String sellerLogicalId = buildSellerLocalId(catalogue);

		String itemLogicalId = sellerLogicalId + "_" + lineLogicalId;

		Product product;
		Optional<Product> optional = productRepository.findById(itemLogicalId);
		if (optional.isPresent()) {
			product = optional.get();
			product.setUpdateTime(Instant.now());
			product.setVersion(product.getVersion() + 1);
			product.setDocumentVersion(ProductDocumentVersion.PEPPOL_1_0);
			product.setDocument(line);
		} else {
			product = new Product();
			product.setId(itemLogicalId);
			product.setCreateTime(Instant.now());
			product.setUpdateTime(null);
			product.setVersion(1);
			product.setDocumentVersion(ProductDocumentVersion.PEPPOL_1_0);
			product.setDocument(line);
		}
		productRepository.save(product);
		return product;
	}

	private String buildSellerLocalId(Catalogue catalogue) {
		if (catalogue.getSellerSupplierParty() != null) {
			Party sellerParty = catalogue.getSellerSupplierParty().getParty();
			if (sellerParty.getPartyIdentification() != null) {
				SchemeID schemeID = sellerParty.getPartyIdentification().getId();
				if (schemeID != null) {
					return schemeID.getLogicalId();
				}
			}
			if (sellerParty.getEndpointID() != null) {
				return sellerParty.getEndpointID().getLogicalId();
			}
		}
		return null;
	}

	public long countItems() {
		return productRepository.count();
	}

	public List<Product> findAll() {
		return productRepository.findAll();
	}

	public Optional<Product> findById(String id) {
		return productRepository.findById(id);
	}
}
