package dk.erst.cm.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.api.item.ProductService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class IndexController {

	@Autowired
	private ProductService productService;

	@GetMapping("/api/status")
	public String index() {
		log.info(null);
		return "OK: " + productService.countItems() + " items";
	}

}
