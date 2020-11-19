package dev.codescreen.ServiceInterfaces;

import dev.codescreen.EventDriven.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* Create an Messaging Queue system to support an event-driven architecture.
* This event queue system should be able to hand off events to multiple computing nodes.
* The service should be as resilient as possible. A system such as MicrosoftMQ or RabbitMQ could be used in place.
* */
public class QueueEventService {

    public static boolean FORCE_ASYNC = true;
    public static boolean DO_NOT_FORCE_ASYNC = false;
    private final static Integer MAX_ATTEMPT_COUNT = 3;

    // mimic the service's event queue
    private static List<Event> ignoreEventList = new ArrayList<>();
    public static LocalTime newEventReceivedTimestamp(){
        return LocalTime.now(); // current server timestamp
    }

    public static RequestResult fireEvent(Event event, boolean forceAsyncProcessing) {
        if (event.getAttemptCount() > MAX_ATTEMPT_COUNT){
            // do not process this event to avoid infinite processing - signal to Ops/Engineering about this failed event
            ignoreEventList.add(event);

            RequestResult failedRequest = new RequestResult() {
                @Override public boolean isSuccess() { return false; }
                @Override public String getMessage() { return "Event attempt limit hit."; }
            };
            return failedRequest;
        }

        if (forceAsyncProcessing){
            // for now, we only implement the synchronous processing
            return TransactionProcessor.processSynchronously(event);
        } else if (event.processSynchronously()){
            return TransactionProcessor.processSynchronously(event);
        } else {
            // process asynsynchronously and return future ID?
            return TransactionProcessor.processSynchronously(event);
        }
    }


    public static RequestResult fireEvent(Event event) {
        return fireEvent(event, DO_NOT_FORCE_ASYNC);
    }

    // KO: using an event builder would be nice - future iteration potential.
    /*
    * Use the EventBuilder to ensure the events are being build correctly.
    * */
//    public static class EventBuilder {
//       private Source eventSource;
//       private UUID eventUuid;
//       private UUID userUuid;
//       private LocalTime receivedTimestamp;
//       private LocalTime finishedProcessingTimestamp;
//       private EventStatus eventStatus;
//
//        public EventBuilder setSource(Source source) {
//            this.eventSource = source;
//            return this;
//        }
//
//        public EventBuilder setEventSource(Source eventSource) { this.eventSource = eventSource; return this;}
//
//        public EventBuilder setUserUuid(UUID userUuid) { this.userUuid = userUuid; return this;  }
//
//        public EventBuilder setEventUuid(UUID eventUuid) { this.eventUuid = eventUuid;  return this;}
//
//        public EventBuilder setReceivedTimestamp(LocalTime receivedTimestamp) { this.receivedTimestamp = receivedTimestamp; return this;}
//
//        public EventBuilder setFinishedProcessingTimestamp(LocalTime finishedProcessingTimestamp) {
//            this.finishedProcessingTimestamp = finishedProcessingTimestamp;
//            return this;
//        }
//
//        public EventBuilder setEventStatus(EventStatus eventStatus) { this.eventStatus = eventStatus; return this; }
//
//        public Event buildLoadRequestEvent(LoadRequest message){
//            UUID eventUuid = UUIDService.getUUIDForClass(LoadRequest.class);
//            return new LoadRequestEvent(eventSource, EventStatus.RECEIVED, userUuid, newEventReceivedTimestamp(), message);
//        }
//    }
}
