package dk.erst.cm.api.dao.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.Item;

public interface ItemRepository extends MongoRepository<Item, String> {

}
