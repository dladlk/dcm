package dk.erst.cm.job;

import dk.erst.cm.AppProperties;
import dk.erst.cm.api.load.FolderLoadService;
import dk.erst.cm.api.util.StatData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class TaskScheduler {

    @Autowired
    private TaskSchedulerMonitor taskSchedulerMonitor;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private FolderLoadService folderLoadService;

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public StatData loadCatalogue() {
        String path = appProperties.getIntegration().getInboxCatalogue();
        return checkAndExecute("loadCatalogue", path, "inboxCatalogue", folder -> folderLoadService.loadCatalogues(folder));
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public StatData loadOrderResponse() {
        String path = appProperties.getIntegration().getInboxOrderResponse();
        return checkAndExecute("loadOrderResponse", path, "inboxOrderResponse", folder -> folderLoadService.loadOrderResponses(folder));
    }

    private interface IExecuteTask {
        StatData execute(File folder);
    }

    protected StatData checkAndExecute(String taskName, String folder, String fieldName, IExecuteTask executeTask) {
        TaskSchedulerMonitor.TaskResult task = taskSchedulerMonitor.build(taskName);
        File folderFile = new File(folder);
        if (!folderFile.exists() || !folderFile.isDirectory()) {
            String error = String.format("Path %s = %s does not exist or is not a directory", fieldName, folderFile.getAbsolutePath());
            log.error("Task {}: {}", taskName, error);
            return StatData.error(error);
        } else {
            try {
                StatData sd = executeTask.execute(folderFile);
                if (!sd.isEmpty()) {
                    String message = "Done loading from folder " + folderFile + " in " + sd.toDurationString() + " with next statistics of document status: " + sd.toStatString();
                    log.info(message);
                }
                task.success(sd);
                return sd;
            } catch (Exception e) {
                log.error("Task " + taskName + ": failed to process folder " + folder, e);
                task.failure(e);
                return StatData.error(e.getMessage());
            }
        }
    }

}
