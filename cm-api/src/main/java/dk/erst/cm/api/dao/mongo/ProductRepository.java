package dk.erst.cm.api.dao.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	List<Product> findByStandardNumber(String standardNumber);

	Long deleteByProductCatalogId(String productCatalogId);

	Long countByProductCatalogId(String id);

}
