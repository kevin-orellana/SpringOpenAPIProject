package dev.codescreen.EventDriven;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/*
* A business event should be abstract enough to hold different types of requests.
* */
public abstract class Event implements EventInterface, HasType {
    private Source eventSource;
    private EventStatus eventStatus;
    private LocalDateTime receivedTimestamp;
    private LocalDateTime startProcessingTime;
    private LocalDateTime endProcessingTime;
    private long wallThread_MS;
    private Integer attemptCount = 0;
    private String machineName;
    public Event() { } // user the Builder pattern to create an event

    public Event(Source eventSource, EventStatus eventStatus) {
        this.eventSource = eventSource;
        this.eventStatus = eventStatus;
        this.receivedTimestamp = LocalDateTime.now();
    }

    public void increaseAttemptCount(){
        attemptCount++;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    @Override
    public boolean processSynchronously() {
        return false;
    }

    @Override
    public Source getSource() {
        return eventSource;
    }

    @Override
    public void setSource(Source source) {
        this.eventSource = source;
    }

    @Override
    public EventStatus getStatus() {
        return eventStatus;
    }

    @Override
    public void setStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public LocalDateTime getStartProcessingTime() {
        return startProcessingTime;
    }

    public void setStartProcessingTime(LocalDateTime startProcessingTime) {
        this.startProcessingTime = startProcessingTime;
    }

    public LocalDateTime getEndProcessingTime() {
        return endProcessingTime;
    }

    public void setEndProcessingTime(LocalDateTime endProcessingTime) {
        this.endProcessingTime = endProcessingTime;
    }

    public void calculateProcessingWallThread(){
        if (startProcessingTime != null && endProcessingTime != null){
            wallThread_MS = ChronoUnit.MILLIS.between(startProcessingTime, endProcessingTime);
        }
    }

    public void setMachineName(String name) {
        machineName = name;
    }
}
