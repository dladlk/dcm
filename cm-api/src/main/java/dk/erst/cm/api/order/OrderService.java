package dk.erst.cm.api.order;

import com.helger.ubl21.UBL21Writer;
import dk.erst.cm.api.dao.mongo.BasketRepository;
import dk.erst.cm.api.dao.mongo.OrderRepository;
import dk.erst.cm.api.data.Basket;
import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.data.OrderStatus;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final BasketRepository basketRepository;
    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public OrderService(MongoTemplate mongoTemplate, BasketRepository basketRepository, OrderRepository orderRepository) {
        this.mongoTemplate = mongoTemplate;
        this.basketRepository = basketRepository;
        this.orderRepository = orderRepository;
    }

    public Optional<Basket> findBasketById(String basketId) {
        return basketRepository.findById(basketId);
    }

    public List<Order> findOrdersByBasketId(String basketId) {
        return orderRepository.findByBasketId(basketId);
    }

    @SuppressWarnings("unused")
    public Optional<Order> findOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    /*
     * If we return to controller an object of OrderType, it is serialized to JSON with Jackson.
     *
     * As a result, 2 different ways to serialize objects are used - MongoConverterConfig to save into MongoDB and Jackson to render on GUI.
     *
     * Moreover, it is quite difficult to configure them to serialize so complex structures as OrderType in the same way,
     * so let's avoid double conversion and use the same approach for both.
     *
     * Finally, it gives us the opportunity to see exact values of MongoDB document fields, useful for querying.
     */
    public Optional<String> findOrderByIdAsJSON(String id) {
        String result = mongoTemplate.execute("order", collection -> {
            Document d = collection.find(new Document("_id", id)).first();
            if (d != null) {
                return d.toJson();
            }
            return null;
        });
        return Optional.ofNullable(result);
    }

    public void saveBasket(Basket basket) {
        this.basketRepository.save(basket);
    }

    public void saveOrder(Order order) {
        this.orderRepository.save(order);
    }

    @SuppressWarnings("unused")
    public void updateOrderStatus(String orderId, OrderStatus status) {
        Optional<Order> optionalOrder = this.orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (status == OrderStatus.DELIVERED) {
                order.setDeliveredDate(Instant.now());
            }
            order.setStatus(status);
            this.orderRepository.save(order);
        }
    }

    public File saveOrderXML(File directory, OrderType sendOrder) throws IOException {
        File tempFile = new File(directory, "delis-cm-" + sendOrder.getIDValue() + ".xml");
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(tempFile.toPath()))) {
            UBL21Writer.order().write(sendOrder, out);
        }
        return tempFile;
    }

}
