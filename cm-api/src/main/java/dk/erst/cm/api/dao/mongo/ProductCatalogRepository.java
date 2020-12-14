package dk.erst.cm.api.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.ProductCatalog;

public interface ProductCatalogRepository extends MongoRepository<ProductCatalog, String> {

}
