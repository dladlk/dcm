package dk.erst.cm.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dk.erst.cm.api.item.ProductService;
import dk.erst.cm.api.load.PeppolLoadService;
import dk.erst.cm.api.load.handler.CatalogConsumer;
import dk.erst.cm.xml.ubl21.model.Catalogue;
import dk.erst.cm.xml.ubl21.model.CatalogueLine;

@RestController
@CrossOrigin(maxAge = 3600)
public class UploadController {

	@Autowired
	private PeppolLoadService loadService;

	@Autowired
	private ProductService productService;

	@PostMapping(value = "/upload")
	public ResponseEntity<Integer> upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		final int[] count = new int[1];
		try {
			final Catalogue[] cat = new Catalogue[1];
			loadService.loadXml(file.getInputStream(), file.getName(), new CatalogConsumer() {
				@Override
				public void consumeHead(Catalogue catalog) {
					cat[0] = catalog;
				}

				@Override
				public void consumeLine(CatalogueLine line) {
					productService.saveCatalogUpdateItem(cat[0], line);
					count[0]++;
				}

			});
		} catch (Exception e) {
			return new ResponseEntity<Integer>(-1, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(count[0], HttpStatus.OK);
	}
}
