package dk.erst.cm.webapi;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.item.ProductService;

@CrossOrigin(maxAge = 3600)
@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/products")
	public List<Product> getAllEmployees() {
		return productService.findAll();
	}

	@RequestMapping(value = "/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
		Optional<Product> findById = productService.findById(id);
		if (findById.isPresent()) {
			return new ResponseEntity<Product>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

}