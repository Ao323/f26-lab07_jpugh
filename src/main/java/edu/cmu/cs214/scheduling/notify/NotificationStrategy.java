package edu.cmu.cs214.scheduling.notify;

/** Turns a message into the text that gets delivered. */
public interface NotificationStrategy {

    String render(NotificationMessage message);
}
