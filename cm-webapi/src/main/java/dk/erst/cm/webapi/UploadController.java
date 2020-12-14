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

import dk.erst.cm.api.item.LoadCatalogService;
import dk.erst.cm.api.load.PeppolLoadService;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(maxAge = 3600)
@Slf4j
public class UploadController {

	@Autowired
	private PeppolLoadService loadService;

	@Autowired
	private LoadCatalogService loadCatalogService;

	@PostMapping(value = "/upload")
	public ResponseEntity<Integer> upload(@RequestParam("files") MultipartFile files[], RedirectAttributes redirectAttributes) {
		int totalLines = 0;
		try {
			for (MultipartFile file : files) {
				log.info("Start reading file " + file.getOriginalFilename());
				FileUploadConsumer fileUploadConsumer = new FileUploadConsumer(loadCatalogService);
				loadService.loadXml(file.getInputStream(), file.getName(), fileUploadConsumer);
				log.info("Loaded file " + file.getOriginalFilename() + " with " + fileUploadConsumer.getLineCount() + " lines");
				totalLines += fileUploadConsumer.getLineCount();
			}
		} catch (Exception e) {
			return new ResponseEntity<Integer>(-1, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Integer>(totalLines, HttpStatus.OK);
	}
}
