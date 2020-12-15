package dk.erst.cm.api.item;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dk.erst.cm.api.dao.mongo.ProductCatalogRepository;
import dk.erst.cm.api.dao.mongo.ProductCatalogUpdateRepository;
import dk.erst.cm.api.dao.mongo.ProductRepository;
import dk.erst.cm.api.data.ProductCatalog;
import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.Party;
import dk.erst.cm.xml.ubl21.model.SchemeID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CatalogService {

	private ProductCatalogRepository productCatalogRepository;
	private ProductCatalogUpdateRepository productCatalogUpdateRepository;
	private ProductRepository productRepository;

	@Autowired
	public CatalogService(ProductCatalogRepository productCatalogRepository, ProductCatalogUpdateRepository productCatalogUpdateRepository, ProductRepository productRepository) {
		this.productCatalogRepository = productCatalogRepository;
		this.productCatalogUpdateRepository = productCatalogUpdateRepository;
		this.productRepository = productRepository;
	}

	public ProductCatalogUpdate saveCatalogue(Catalogue catalogue) {
		String sellerLocalId = buildSellerLocalId(catalogue);

		ProductCatalog productCatalog = productCatalogRepository.findById(sellerLocalId).orElse(null);
		if (productCatalog == null) {
			productCatalog = new ProductCatalog();
			productCatalog.setId(sellerLocalId);
			productCatalog.setCreateTime(Instant.now());
			productCatalog.setVersion(1);
		} else {
			productCatalog.setUpdateTime(Instant.now());
			productCatalog.setVersion(productCatalog.getVersion() + 1);
		}

		productCatalogRepository.save(productCatalog);

		ProductCatalogUpdate c = new ProductCatalogUpdate();
		c.setId(UUID.randomUUID().toString());
		c.setCreateTime(Instant.now());
		c.setDocumentVersion(ProductDocumentVersion.PEPPOL_CATALOGUE_3_1);
		c.setDocument(catalogue);
		c.setProductCatalogId(productCatalog.getId());

		productCatalogUpdateRepository.save(c);

		if (catalogue.getActionCode() != null) {
			if ("Delete".equalsIgnoreCase(catalogue.getActionCode())) {
				Long countProducts = productRepository.countByProductCatalogId(productCatalog.getId());
				log.info("Found " + countProducts + " in catalogue " + productCatalog.getId());

				Long deletedProducts = productRepository.deleteByProductCatalogId(productCatalog.getId());
				log.info("Deleted " + deletedProducts + " products from catalog " + productCatalog.getId());

				productCatalog.setLineCount(0);
				productCatalogRepository.save(productCatalog);
			}
		}

		return c;
	}

	private String buildSellerLocalId(Catalogue catalogue) {
		String sellerLogicalId = null;
		if (catalogue.getSellerSupplierParty() != null) {
			sellerLogicalId = buildPartyId(catalogue.getSellerSupplierParty().getParty());
		}
		if (sellerLogicalId == null) {
			sellerLogicalId = buildPartyId(catalogue.getProviderParty());
		}
		if (sellerLogicalId == null) {
			sellerLogicalId = "GLOBAL";
		}
		return sellerLogicalId;
	}

	private String buildPartyId(Party party) {
		if (party == null) {
			return null;
		}
		if (party.getPartyLegalEntity() != null) {
			if (party.getPartyLegalEntity().getCompanyID() != null) {
				return logicalSchemeId(party.getPartyLegalEntity().getCompanyID());
			}
		}
		if (party.getPartyIdentification() != null) {
			SchemeID schemeID = party.getPartyIdentification().getId();
			if (schemeID != null) {
				return logicalSchemeId(schemeID);
			}
		}
		if (party.getEndpointID() != null) {
			return logicalSchemeId(party.getEndpointID());
		}
		return null;
	}

	public String logicalSchemeId(SchemeID sid) {
		if (sid.getSchemeId() != null) {
			return (sid.getSchemeId() + ":" + sid.getId()).toUpperCase();
		}
		return sid.getId().toUpperCase();
	}
}
