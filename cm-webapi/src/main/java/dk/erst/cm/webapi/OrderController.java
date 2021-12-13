package dk.erst.cm.webapi;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.AppProperties;
import dk.erst.cm.api.data.Order;
import dk.erst.cm.api.order.BasketService;
import dk.erst.cm.api.order.BasketService.SendBasketData;
import dk.erst.cm.api.order.BasketService.SendBasketResponse;
import dk.erst.cm.api.order.BasketService.SentBasketData;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
public class OrderController {

	private final BasketService basketService;
	private final AppProperties appProperties;

	@Autowired
	public OrderController(BasketService basketService, AppProperties appProperties) {
		this.basketService = basketService;
		this.appProperties = appProperties;
	}

	@RequestMapping(value = "/api/basket/send")
	public SendBasketResponse basketSend(@RequestBody SendBasketData query) {
		log.info("START basketSend: query=" + query);
		SendBasketResponse res = basketService.basketSend(query, appProperties.getIntegration().getOutboxOrder(), appProperties.getOrder());
		if (res.isSuccess()) {
			log.info("END basketSend OK: " + res);
		} else {
			log.info("END basketSend Error: " + res);
		}
		return res;
	}

	@RequestMapping(value = "/api/basket/{id}")
	public ResponseEntity<SentBasketData> getBasketById(@PathVariable("id") String id) {
		Optional<SentBasketData> findById = basketService.loadSentBasketData(id);
		if (findById.isPresent()) {
			return new ResponseEntity<>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/api/order/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") String id) {
		Optional<Order> findById = basketService.loadSentOrder(id);
		if (findById.isPresent()) {
			return new ResponseEntity<>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

}
