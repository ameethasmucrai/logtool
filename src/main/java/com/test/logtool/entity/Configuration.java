package com.test.logtool.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Used to read the configurations in application.properties. Managed by Spring
 */
@ConfigurationProperties(prefix = "config")
public class Configuration {

    private long eventDurationAlertThreshold;
    private int processorThreads;
    private String fileEncoding;
    private String stateFinished;
    private long awaitTermination;
    private long waitTimeCheckPair;

    public long getEventDurationAlertThreshold() {
        return eventDurationAlertThreshold;
    }

    public void setEventDurationAlertThreshold(long eventDurationAlertThreshold) {
        this.eventDurationAlertThreshold = eventDurationAlertThreshold;
    }

    public int getProcessorThreads() {
        return processorThreads;
    }

    public void setProcessorThreads(int processorThreads) {
        this.processorThreads = processorThreads;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }

    public String getStateFinished() {
        return stateFinished;
    }

    public void setStateFinished(String stateFinished) {
        this.stateFinished = stateFinished;
    }

    public long getAwaitTermination() {
        return awaitTermination;
    }

    public void setAwaitTermination(long awaitTermination) {
        this.awaitTermination = awaitTermination;
    }

    public long getWaitTimeCheckPair() {
        return waitTimeCheckPair;
    }

    public void setWaitTimeCheckPair(long waitTimeCheckPair) {
        this.waitTimeCheckPair = waitTimeCheckPair;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "eventDurationAlertThreshold=" + eventDurationAlertThreshold +
                ", processorThreads=" + processorThreads +
                ", fileEncoding='" + fileEncoding + '\'' +
                ", stateFinished='" + stateFinished + '\'' +
                ", awaitTermination=" + awaitTermination +
                ", waitTimeCheckPair=" + waitTimeCheckPair +
                '}';
    }

}
