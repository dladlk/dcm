package dk.erst.cm.api.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.ProductCatalogUpdate;

public interface ProductCatalogUpdateRepository extends MongoRepository<ProductCatalogUpdate, String> {

}
