package edu.cmu.cs214.scheduling.domain;

import java.time.LocalDateTime;

/**
 * What a caller hands to the workflow.
 *
 * <p>{@code occurrences} counts weekly repeats and only applies to recurring
 * requests. {@code memberId} is null on an administrative block.
 */
public record BookingRequest(BookingType type, String roomId, String memberId, TimeSlot slot,
                             int occurrences, int attendees) {

    public BookingRequest {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (roomId == null || roomId.isBlank()) {
            throw new IllegalArgumentException("roomId must not be blank");
        }
        if (slot == null) {
            throw new IllegalArgumentException("slot must not be null");
        }
    }

    public static BookingRequest regular(String roomId, String memberId, LocalDateTime start,
                                         LocalDateTime end, int attendees) {
        return new BookingRequest(BookingType.REGULAR, roomId, memberId,
                new TimeSlot(start, end), 1, attendees);
    }

    public static BookingRequest recurring(String roomId, String memberId, LocalDateTime start,
                                           LocalDateTime end, int occurrences, int attendees) {
        return new BookingRequest(BookingType.RECURRING, roomId, memberId,
                new TimeSlot(start, end), occurrences, attendees);
    }

    public static BookingRequest blocked(String roomId, LocalDateTime start, LocalDateTime end) {
        return new BookingRequest(BookingType.BLOCKED, roomId, null,
                new TimeSlot(start, end), 1, 0);
    }
}
