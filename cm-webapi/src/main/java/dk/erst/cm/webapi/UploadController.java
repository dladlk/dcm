package dk.erst.cm.webapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dk.erst.cm.api.data.ProductCatalogUpdate;
import dk.erst.cm.api.item.LoadCatalogService;
import dk.erst.cm.api.load.PeppolLoadService;
import dk.erst.cm.webapi.FileUploadConsumer.LineAction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(maxAge = 3600)
@Slf4j
public class UploadController {

	@Autowired
	private PeppolLoadService loadService;

	@Autowired
	private LoadCatalogService loadCatalogService;

	@PostMapping(value = "/api/upload")
	public ResponseEntity<List<UploadResult>> upload(@RequestParam("files") MultipartFile files[], RedirectAttributes redirectAttributes) {
		List<UploadResult> uploadResultList = new ArrayList<UploadController.UploadResult>();
		for (MultipartFile file : files) {
			UploadResult ur = new UploadResult();
			ur.setFileName(file.getOriginalFilename());

			FileUploadConsumer fileUploadConsumer = new FileUploadConsumer(loadCatalogService);
			try {
				log.info("Start reading file " + file.getOriginalFilename());
				loadService.loadXml(file.getInputStream(), file.getName(), fileUploadConsumer);
				log.info("Loaded file " + file.getOriginalFilename() + " with " + fileUploadConsumer.getLineCount() + " lines");

				ur.copyFromConsumer(fileUploadConsumer);
				ur.setSuccess(true);

			} catch (Exception e) {
				ur.copyFromConsumer(fileUploadConsumer);
				ur.setSuccess(false);
				ur.setErrorMessage(e.getMessage());
			}

			uploadResultList.add(ur);
		}
		return new ResponseEntity<>(uploadResultList, HttpStatus.OK);
	}

	@Getter
	@Setter
	public static class UploadResult {
		String fileName;
		ProductCatalogUpdate productCatalogUpdate;
		boolean success;
		String errorMessage;
		int lineCount;
		Map<LineAction, Integer> lineActionStat;

		protected void copyFromConsumer(FileUploadConsumer fileUploadConsumer) {
			this.setLineActionStat(fileUploadConsumer.getLineActionStat());
			this.setLineCount(fileUploadConsumer.getLineCount());
			this.setProductCatalogUpdate(fileUploadConsumer.getProductCatalogUpdate());
		}
	}
}
