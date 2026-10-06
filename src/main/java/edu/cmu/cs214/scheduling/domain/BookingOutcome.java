package edu.cmu.cs214.scheduling.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The result of a submission.
 *
 * <p>Carries every booking that was written and every slot that was not, so a
 * caller can report both without a second query.
 */
public class BookingOutcome {

    private final boolean accepted;
    private final List<Booking> booked;
    private final List<TimeSlot> skipped;
    private final String message;

    private BookingOutcome(boolean accepted, List<Booking> booked, List<TimeSlot> skipped,
                           String message) {
        this.accepted = accepted;
        this.booked = Collections.unmodifiableList(new ArrayList<>(booked));
        this.skipped = Collections.unmodifiableList(new ArrayList<>(skipped));
        this.message = message;
    }

    public static BookingOutcome rejected(String message) {
        return new BookingOutcome(false, List.of(), List.of(), message);
    }

    public static BookingOutcome confirmed(Booking booking, String message) {
        return new BookingOutcome(true, List.of(booking), List.of(), message);
    }

    /** Accepted when at least one slot was written. */
    public static BookingOutcome series(List<Booking> booked, List<TimeSlot> skipped,
                                        String message) {
        return new BookingOutcome(!booked.isEmpty(), booked, skipped, message);
    }

    public boolean isAccepted() {
        return accepted;
    }

    public List<Booking> getBooked() {
        return booked;
    }

    public List<TimeSlot> getSkipped() {
        return skipped;
    }

    public String getMessage() {
        return message;
    }

    /** The first booking written, or null when nothing was written. */
    public Booking getBooking() {
        return booked.isEmpty() ? null : booked.get(0);
    }

    @Override
    public String toString() {
        return (accepted ? "accepted" : "rejected") + ": " + message;
    }
}
