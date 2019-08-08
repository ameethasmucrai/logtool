package com.test.logtool.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity to log the event to the database
 */
@Entity(name="event")
public class EventEntity {

    @Id
    private String eventId;
    private long eventDuration;
    private String type;
    private String host;
    private boolean alert;

    public EventEntity() {
    }

    public EventEntity(String eventId, long eventDuration, String type, String host, boolean alert) {
        this.eventId = eventId;
        this.eventDuration = eventDuration;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(long eventDuration) {
        this.eventDuration = eventDuration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    @Override
    public String toString() {
        return "EventEntity{" +
                "eventId='" + eventId + '\'' +
                ", eventDuration=" + eventDuration +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", alert=" + alert +
                '}';
    }

}
