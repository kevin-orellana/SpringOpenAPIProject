package dev.codescreen.EventDriven;

public interface EventInterface extends HasSource {

    public EventStatus getStatus();
    public void setStatus(EventStatus eventStatus);

    public boolean processSynchronously();
}
