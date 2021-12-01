package dk.erst.cm.webapi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping(value = "/api/products")
	public Page<Product> getProducts(@RequestParam(required = false) String search, Pageable pageable) {
		log.info("Search products by " + search + ", page " + pageable);
		return productService.findAll(search, pageable);
	}

	@Data
	public static class IdList {
		private String[] ids;
	}

	@RequestMapping(value = "/api/products_by_ids")
	public Iterable<Product> getProductsByIds(@RequestBody IdList query) {
		log.info("Search products by ids " + query);
		return productService.findAllByIds(Arrays.asList(query.ids));
	}

	@RequestMapping(value = "/api/product/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") String id) {
		Optional<Product> findById = productService.findById(id);
		if (findById.isPresent()) {
			return new ResponseEntity<>(findById.get(), HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

	@RequestMapping(value = "/api/products/{id}")
	public ResponseEntity<List<Product>> getProductsById(@PathVariable("id") String id) {
		Optional<Product> findById = productService.findById(id);
		if (findById.isPresent()) {
			Product product = findById.get();
			List<Product> list;
			if (!StringUtils.isEmpty(product.getStandardNumber())) {
				list = productService.findByStandardNumber(product.getStandardNumber());
			} else {
				list = Collections.singletonList(product);
			}
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		return ResponseEntity.notFound().build();
	}

}
