package dk.erst.cm;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

import dk.erst.cm.api.data.Product;

@Configuration
@DependsOn("mongoTemplate")
public class MongoConfig {

	@Autowired
	private MongoTemplate mongoTemplate;

	@PostConstruct
	public void initIndexes() {
		TextIndexDefinition textIndexDefinition = TextIndexDefinition.builder().onFields(Product.FULL_TEXT_SEARCH_FIELDS).withDefaultLanguage("danish").build();
		mongoTemplate.indexOps(Product.class).ensureIndex(textIndexDefinition);
	}
}
