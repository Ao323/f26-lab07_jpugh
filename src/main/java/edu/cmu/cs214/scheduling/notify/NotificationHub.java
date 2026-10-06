package edu.cmu.cs214.scheduling.notify;

import java.util.ArrayList;
import java.util.List;

/** Renders published messages and hands the text to its subscribers. */
public class NotificationHub {

    private final NotificationStrategy strategy;
    private final List<NotificationSubscriber> subscribers = new ArrayList<>();
    private final Outbox outbox;

    public NotificationHub() {
        this(new Outbox());
    }

    public NotificationHub(Outbox outbox) {
        if (outbox == null) {
            throw new IllegalArgumentException("outbox must not be null");
        }
        this.outbox = outbox;
        this.strategy = NotifierFactory.getInstance().createStrategy();
        subscribe(new OutboxSubscriber(outbox));
    }

    public void subscribe(NotificationSubscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("subscriber must not be null");
        }
        subscribers.add(subscriber);
    }

    public void publish(NotificationMessage message) {
        String rendered = strategy.render(message);
        for (NotificationSubscriber subscriber : subscribers) {
            subscriber.onNotification(rendered);
        }
    }

    public Outbox getOutbox() {
        return outbox;
    }

    public int subscriberCount() {
        return subscribers.size();
    }
}
