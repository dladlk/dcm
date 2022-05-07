package dk.erst.cm.api.load;

import dk.erst.cm.api.item.LoadCatalogService;
import dk.erst.cm.api.load.handler.FileUploadConsumer;
import dk.erst.cm.api.util.StatData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class FolderLoadService {

    private static final File[] EMPTY_FILE_LIST = new File[0];
    @Autowired
    private LoadCatalogService loadCatalogService;
    @Autowired
    private PeppolLoadService loadService;

    public StatData loadCatalogues(File folder) {
        StatData statData = new StatData();
        log.debug("Start load from folder {}", folder.getAbsolutePath());
        Optional<File[]> optionalFiles = Optional.ofNullable(folder.listFiles((dir, fileName) -> fileName.toLowerCase().endsWith(".xml")));
        Arrays.stream(optionalFiles.orElse(EMPTY_FILE_LIST)).forEach(file -> {
            FileUploadConsumer fileUploadConsumer = new FileUploadConsumer(loadCatalogService);
            try {
                log.info("Start reading file {}", file.getName());
                File tempFile = new File(file.getAbsolutePath() + ".tmp");
                if (!file.renameTo(tempFile)){
                    log.debug("Could not rename file {}, skip it", file.getName());
                    return;
                }
                try (InputStream inputStream = Files.newInputStream(tempFile.toPath())) {
                    loadService.loadXml(inputStream, file.getName(), fileUploadConsumer);
                }
                log.info("Loaded file " + file.getName() + " with " + fileUploadConsumer.getLineCount() + " lines");
                statData.increment("loaded-files");
                statData.increase("loaded-lines", fileUploadConsumer.getLineCount());
                if (tempFile.delete()) {
                    tempFile.deleteOnExit();
                }
            } catch (Exception e) {
                log.error("Error loading file " + file.getName(), e);
                statData.increment("error-"+e.getClass().getName());
            }
        });
        return statData;
    }
    public StatData loadOrderResponses(File folder) {
        log.debug("Start load from folder {}", folder.getAbsolutePath());
        return StatData.error("Not implemented");
    }
}
