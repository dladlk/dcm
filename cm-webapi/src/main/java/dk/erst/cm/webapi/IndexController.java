package dk.erst.cm.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.api.item.ItemService;

@RestController
public class IndexController {

	@Autowired
	private ItemService itemService;

	@GetMapping("/")
	public String index() {
		return "OK: " + itemService.countItems() + " items";
	}

}
