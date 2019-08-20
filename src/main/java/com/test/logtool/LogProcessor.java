package com.test.logtool;

import com.test.logtool.entity.Configuration;
import com.test.logtool.entity.EventEntity;
import com.test.logtool.entity.LogEntity;
import com.test.logtool.repository.EventEntityRepository;
import com.test.logtool.utils.HasLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;

/**
 * Waits for the correspoding log pair, calculates the event duration, flags alerts and saves to database
 * Processes in separate thread each unique log id
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogProcessor implements HasLogger, Runnable {

    @Autowired
    Configuration configuration;

    @Autowired
    private EventEntityRepository eventEntityRepository;

    private LogEntity firstEntry;

    boolean waitingForThePair;

    public LogProcessor() {
    }

    /**
     * Constructor to receive the log entry to wait for the pair
     * @param entry log entry to wait for the pair
     */
    public LogProcessor(LogEntity entry) {
        this.firstEntry = entry;
    }

    /**
     * Waits until the log pair is available, calculates the duration of the event, flags any alert and saves to the database
     */
    public void processEntry() {

        LogEntity secondEntry = LogReader.secondEntryHashMap.get(firstEntry.getId());

        if (secondEntry != null) {

            String eventId = secondEntry.getId();
            long eventDuration;
            boolean alert = false;

            if (StringUtils.capitalize(firstEntry.getState()).equals(configuration.getStateFinished())) {
                eventDuration = firstEntry.getTimestamp() - secondEntry.getTimestamp();
            } else {
                eventDuration = secondEntry.getTimestamp() - firstEntry.getTimestamp();
            }

            // Write the found event details to file-based HSQLDB (http://hsqldb.org/) in the working folder
            // Flag any long events that take longer than x ms with a column in the database called "alert"
            alert = eventDuration > configuration.getEventDurationAlertThreshold() ? true : false;
            if (alert) {
                getLogger().warn(MessageFormat.format("Time elapsed for event ID {0}: {1}", eventId, eventDuration));
            }
            else {
                getLogger().debug(MessageFormat.format("Time elapsed for event ID {0}: {1}", eventId, eventDuration));
            }

            EventEntity eventEntity = new EventEntity(eventId, eventDuration, firstEntry.getType(), firstEntry.getHost(), alert);
            eventEntityRepository.save(eventEntity);

            // Only remove from hash after writing to DB
            LogReader.firstEntryHashMap.remove(eventId);
            LogReader.secondEntryHashMap.remove(eventId);

            waitingForThePair = false;

        }

    }

    /**
     * Thread runner
     */
    @Override
    public void run() {
        getLogger().debug("Process entry ");
        getLogger().debug("Entry to watch for " + firstEntry);
        waitingForThePair = true;
        while (waitingForThePair) {
            processEntry();
            try {
                Thread.sleep(configuration.getWaitTimeCheckPair());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

