package edu.cmu.cs214.scheduling.notify;

/** Renders a message as a one-line mail header plus body. */
public class EmailNotificationStrategy implements NotificationStrategy {

    @Override
    public String render(NotificationMessage message) {
        return "To: " + message.recipient()
                + " | Subject: " + message.subject()
                + " | " + message.body();
    }
}
