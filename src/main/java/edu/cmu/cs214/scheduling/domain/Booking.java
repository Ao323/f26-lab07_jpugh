package edu.cmu.cs214.scheduling.domain;

import java.time.LocalDateTime;

/**
 * One reserved slot in one room.
 *
 * <p>Recurring occurrences carry a series id and a one-based occurrence index;
 * every other kind leaves those null and zero.
 */
public class Booking {

    private final long id;
    private final String roomId;
    private final String memberId;
    private final TimeSlot slot;
    private final BookingType type;
    private final String seriesId;
    private final int occurrenceIndex;
    private boolean cancelled;

    public Booking(long id, String roomId, String memberId, TimeSlot slot, BookingType type,
                   String seriesId, int occurrenceIndex) {
        if (roomId == null || roomId.isBlank()) {
            throw new IllegalArgumentException("roomId must not be blank");
        }
        if (slot == null) {
            throw new IllegalArgumentException("slot must not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        this.id = id;
        this.roomId = roomId;
        this.memberId = memberId;
        this.slot = slot;
        this.type = type;
        this.seriesId = seriesId;
        this.occurrenceIndex = occurrenceIndex;
    }

    public long getId() {
        return id;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getMemberId() {
        return memberId;
    }

    public TimeSlot getSlot() {
        return slot;
    }

    public LocalDateTime getStart() {
        return slot.start();
    }

    public LocalDateTime getEnd() {
        return slot.end();
    }

    public BookingType getType() {
        return type;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public int getOccurrenceIndex() {
        return occurrenceIndex;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    @Override
    public String toString() {
        return type + " #" + id + " " + roomId + " " + slot + (cancelled ? " [cancelled]" : "");
    }
}
