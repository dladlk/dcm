package dk.erst.cm.api.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	Long deleteByProductCatalogId(String productCatalogId);

}
