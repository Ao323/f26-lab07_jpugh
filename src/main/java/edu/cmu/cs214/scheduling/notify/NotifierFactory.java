package edu.cmu.cs214.scheduling.notify;

/** Builds the renderer used for outbound notifications. */
public final class NotifierFactory {

    private static NotifierFactory instance;

    private NotifierFactory() {
    }

    public static synchronized NotifierFactory getInstance() {
        if (instance == null) {
            instance = new NotifierFactory();
        }
        return instance;
    }

    /** Email formatting. */
    public NotificationStrategy createStrategy() {
        return new EmailNotificationStrategy();
    }
}
