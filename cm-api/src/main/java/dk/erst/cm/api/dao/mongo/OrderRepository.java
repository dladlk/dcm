package dk.erst.cm.api.dao.mongo;

import dk.erst.cm.api.data.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {

}
