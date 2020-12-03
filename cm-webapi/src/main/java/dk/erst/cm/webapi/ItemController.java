package dk.erst.cm.webapi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.api.data.Item;
import dk.erst.cm.api.item.ItemService;

@RestController
public class ItemController {

	@Autowired
	private ItemService itemService;

	@RequestMapping(value = "/items")
	public List<Item> getAllEmployees() {
		return itemService.findAll();
	}

	@RequestMapping(value = "/item/{id}")
	public ResponseEntity<Item> getEmployeeById(@PathVariable("id") String id) {
		Optional<Item> findById = itemService.findById(id);
		if (findById.isPresent()) {
			return new ResponseEntity<Item>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

}
