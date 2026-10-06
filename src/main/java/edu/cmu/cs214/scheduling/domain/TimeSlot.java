package edu.cmu.cs214.scheduling.domain;

import java.time.Duration;
import java.time.LocalDateTime;

/** A time range with an inclusive start and an exclusive end. */
public record TimeSlot(LocalDateTime start, LocalDateTime end) {

    public TimeSlot {
        if (start == null || end == null) {
            throw new IllegalArgumentException("slot bounds must not be null");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("slot start must come before slot end");
        }
    }

    /** Length of the slot in whole minutes. */
    public long minutes() {
        return Duration.between(start, end).toMinutes();
    }

    /** The same time of day and length, shifted forward by whole weeks. */
    public TimeSlot plusWeeks(int weeks) {
        return new TimeSlot(start.plusWeeks(weeks), end.plusWeeks(weeks));
    }

    @Override
    public String toString() {
        return start + " to " + end;
    }
}
