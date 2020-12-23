package dk.erst.cm.webapi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dk.erst.cm.api.data.Product;
import dk.erst.cm.api.item.ProductService;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(maxAge = 3600)
@RestController
@Slf4j
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/products")
	public Page<Product> getProducts(@RequestParam(required = false) String search, Pageable pageable) {
		log.info("Search products by " + search + ", page " + pageable);
		return productService.findAll(search, pageable);
	}

	@RequestMapping(value = "/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
		Optional<Product> findById = productService.findById(id);
		if (findById.isPresent()) {
			return new ResponseEntity<Product>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/products/{id}")
	public ResponseEntity<List<Product>> getProductsById(@PathVariable("id") String id) {
		Optional<Product> findById = productService.findById(id);
		if (findById.isPresent()) {
			Product product = findById.get();
			List<Product> list;
			if (!StringUtils.isEmpty(product.getStandardNumber())) {
				list = productService.findByStandardNumber(product.getStandardNumber());
			} else {
				list = Arrays.asList(product);
			}
			return new ResponseEntity<List<Product>>(list, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

}
