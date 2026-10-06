package edu.cmu.cs214.scheduling.notify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Everything the scheduler has sent, in order. */
public class Outbox {

    private final List<String> messages = new ArrayList<>();

    public void append(String rendered) {
        messages.add(rendered);
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    public int size() {
        return messages.size();
    }

    /** The most recent message, or null when nothing has been sent. */
    public String last() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    public void clear() {
        messages.clear();
    }
}
