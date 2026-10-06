package edu.cmu.cs214.scheduling.notify;

/** Writes what the hub publishes into an outbox. */
public class OutboxSubscriber implements NotificationSubscriber {

    private final Outbox outbox;

    public OutboxSubscriber(Outbox outbox) {
        if (outbox == null) {
            throw new IllegalArgumentException("outbox must not be null");
        }
        this.outbox = outbox;
    }

    @Override
    public void onNotification(String rendered) {
        outbox.append(rendered);
    }
}
