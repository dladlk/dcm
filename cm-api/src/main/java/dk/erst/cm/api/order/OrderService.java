package dk.erst.cm.api.order;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helger.ubl21.UBL21Writer;

import dk.erst.cm.api.dao.mongo.BasketRepository;
import dk.erst.cm.api.dao.mongo.OrderRepository;
import dk.erst.cm.api.data.Basket;
import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.data.OrderStatus;
import oasis.names.specification.ubl.schema.xsd.order_21.OrderType;

@Service
public class OrderService {

	private final BasketRepository basketRepository;
	private final OrderRepository orderRepository;

	@Autowired
	public OrderService(BasketRepository basketRepository, OrderRepository orderRepository) {
		this.basketRepository = basketRepository;
		this.orderRepository = orderRepository;
	}

	public Optional<Basket> findBasketById(String basketId) {
		return basketRepository.findById(basketId);
	}

	public List<Order> findOrdersByBasketId(String basketId) {
		return orderRepository.findByBasketId(basketId);
	}

	public Optional<Order> findOrderById(String orderId) {
		return orderRepository.findById(orderId);
	}

	public void saveBasket(Basket basket) {
		this.basketRepository.save(basket);
	}

	public void saveOrder(Order order) {
		this.orderRepository.save(order);
	}

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
		try (OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile))) {
			UBL21Writer.order().write(sendOrder, out);
		}
		return tempFile;
	}

}
