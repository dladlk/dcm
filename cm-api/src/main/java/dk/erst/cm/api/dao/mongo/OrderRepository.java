package dk.erst.cm.api.dao.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import dk.erst.cm.api.data.Order;

public interface OrderRepository extends MongoRepository<Order, String> {

	List<Order> findByBasketId(String basketId);

}
