package dev.codescreen.EventDriven;

/*
* The different types of status an event being processed by the system may have.
* */
public enum  EventStatus {
    RECEIVED,
    PROCESSING,
    PROCESSED,
    REQUEUED
}
