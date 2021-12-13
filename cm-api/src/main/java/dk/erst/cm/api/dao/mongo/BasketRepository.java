package dk.erst.cm.api.dao.mongo;

import dk.erst.cm.api.data.Basket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BasketRepository extends MongoRepository<Basket, String> {

}
