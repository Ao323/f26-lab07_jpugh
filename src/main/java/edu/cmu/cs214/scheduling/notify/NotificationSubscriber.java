package edu.cmu.cs214.scheduling.notify;

/** Receives rendered notification text from the hub. */
public interface NotificationSubscriber {

    void onNotification(String rendered);
}
