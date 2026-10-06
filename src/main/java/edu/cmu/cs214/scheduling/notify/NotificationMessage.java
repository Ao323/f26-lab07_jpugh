package edu.cmu.cs214.scheduling.notify;

import java.time.LocalDateTime;

/** One thing worth telling somebody about. */
public record NotificationMessage(String recipient, String subject, String body,
                                  LocalDateTime occurredAt) {

    public NotificationMessage {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("recipient must not be blank");
        }
        if (subject == null) {
            throw new IllegalArgumentException("subject must not be null");
        }
    }
}
