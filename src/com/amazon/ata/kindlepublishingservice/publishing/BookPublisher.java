package com.amazon.ata.kindlepublishingservice.publishing;

import com.google.common.annotations.VisibleForTesting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class responsible for executing publishing tasks. The publisher is created in an off state. A call to start will
 * start the publishing process.
 */
@Singleton
public class BookPublisher {

    private static final Logger log = LogManager.getLogger(BookPublisher.class);

    private final ScheduledExecutorService scheduledExecutorService;

    private boolean isRunning;
    private BookPublishTask bookPublishTask;
    private NoOpTask noOpTask;


    /**
     * Instantiates a new BookPublisher object.
     *
     * @param scheduledExecutorService will schedule publishing tasks
     * @param bookPublishTask the task that should be scheduled to publish
     */
    @Inject
    public BookPublisher(ScheduledExecutorService scheduledExecutorService,
                         BookPublishTask bookPublishTask) {
//        this.noOpTask = noOpTask;
        this.bookPublishTask = bookPublishTask;
        this.scheduledExecutorService = scheduledExecutorService;

    }

    /**
     * Start publishing books.
     */
    public void start() {
        if (isRunning) {
            return;
        }
//        List<Thread> bookPublisherThreads = new ArrayList<>();
        isRunning = true;
        Thread aThread = new Thread(bookPublishTask);
//        bookPublisherThreads.add(aThread);


        aThread.run();

        scheduledExecutorService.scheduleWithFixedDelay(aThread, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Stop publishing books.
     */
    public void stop() {
        isRunning = false;
        scheduledExecutorService.shutdown();
    }

    /**
     * Returns true if the publisher is currently working to publish books and false otherwise.
     * @return if the publisher is currently processing.
     */
    @VisibleForTesting
    boolean isRunning() {
        return isRunning;
    }
}
