package dk.erst.cm.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.FixedDelayTask;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class SchedulerConfig implements SchedulingConfigurer {
    private static final int POOL_SIZE = 2;

    @Value("${job.interval.load-catalogue:-1}")
    private long loadCatalogue;
    @Value("${job.interval.load-order-response:-1}")
    private long loadOrderResponse;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        List<IntervalTask> taskList = scheduledTaskRegistrar.getFixedDelayTaskList();

        List<IntervalTask> newTaskList = new ArrayList<>();
        for (IntervalTask intervalTask : taskList) {
            long interval = getExpectedInterval(intervalTask);
            if (interval < 0) {
                log.info("Skip interval task " + intervalTask);
                continue;
            }
            FixedDelayTask newTask = new FixedDelayTask(intervalTask.getRunnable(), interval, interval);
            newTaskList.add(newTask);
            log.info("Set interval and delay to " + interval + " for " + newTask);
        }
        scheduledTaskRegistrar.setFixedDelayTasksList(newTaskList);

        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("tasks-");
        threadPoolTaskScheduler.initialize();
    }

    private long getExpectedInterval(IntervalTask intervalTask) {
        String t = intervalTask.toString();
        if (t.endsWith("loadCatalogue")) {
            return this.loadCatalogue * 1000;
        }
        if (t.endsWith("loadOrderResponse")) {
            return this.loadOrderResponse * 1000;
        }
        return Long.MAX_VALUE;
    }

}
