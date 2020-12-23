package dk.erst.cm.api.dao.mongo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	List<Product> findByStandardNumber(String standardNumber);

	Long deleteByProductCatalogId(String productCatalogId);

	Long countByProductCatalogId(String id);

	Page<Product> findAllBy(TextCriteria criteria, Pageable pageable);

}
