package dk.erst.cm.api.dao.es;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductESRepository extends ElasticsearchRepository<ProductES, String> {

	Page<ProductES> findByNameOrDescriptionOrCertificatesOrOriginOrStandardNumberOrCategoriesOrKeywords(String name, String description, String certificates, String origin, String standardNumber, String categories, String keywords, Pageable pageagble);

}
