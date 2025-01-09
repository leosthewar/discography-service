package com.clara.discographyservice.application.domain.service;

import com.clara.discographyservice.application.port.in.ImportReleasesAsyncUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.ReentrantLock;

@Service
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.import.releases.enabled", havingValue = "true", matchIfMissing =true)
class ImportReleasesScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ImportReleasesScheduledTask.class);

    private final ImportReleasesAsyncUseCase importReleasesAsyncUseCase;

    public ImportReleasesScheduledTask(ImportReleasesAsyncUseCase importReleasesAsyncUseCase) {
        this.importReleasesAsyncUseCase = importReleasesAsyncUseCase;
    }

    private final ReentrantLock lock = new ReentrantLock();

    @Scheduled(fixedDelay = 60000, initialDelay = 2000) // Every minute
    public void executeTask(){
        if (lock.tryLock()) { // Prevent concurrent executions
            try {
                logger.info("ScheduledTask started");
                importReleasesAsyncUseCase.executeImportation();
                logger.info("ScheduledTask finished");
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                logger.error("ScheduledTask interrupted", e);
            } finally {
                lock.unlock(); // Release the lock
            }
        } else {
            logger.debug("ScheduledTask already running, skipping execution");
        }
    }
}
