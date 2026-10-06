package edu.cmu.cs214.scheduling.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** In-memory catalogue of rooms, members, and bookings. */
public class BookingStore {

    private final Map<String, Room> rooms = new LinkedHashMap<>();
    private final Map<String, Member> members = new LinkedHashMap<>();
    private final List<Booking> bookings = new ArrayList<>();
    private long nextBookingId = 1;
    private long nextSeriesNumber = 1;

    public void addRoom(Room room) {
        rooms.put(room.getId(), room);
    }

    public void addMember(Member member) {
        members.put(member.getId(), member);
    }

    public Room findRoom(String roomId) {
        return roomId == null ? null : rooms.get(roomId);
    }

    public Member findMember(String memberId) {
        return memberId == null ? null : members.get(memberId);
    }

    public List<Room> allRooms() {
        return new ArrayList<>(rooms.values());
    }

    public long nextBookingId() {
        return nextBookingId++;
    }

    public String nextSeriesId() {
        return "S-" + nextSeriesNumber++;
    }

    public void save(Booking booking) {
        bookings.add(booking);
    }

    public Booking findBooking(long bookingId) {
        for (Booking booking : bookings) {
            if (booking.getId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    /** Every booking ever written, cancelled ones included, in creation order. */
    public List<Booking> allBookings() {
        return new ArrayList<>(bookings);
    }

    /** Live bookings in one room, earliest first. */
    public List<Booking> activeInRoom(String roomId) {
        List<Booking> result = new ArrayList<>();
        for (Booking booking : bookings) {
            if (!booking.isCancelled() && booking.getRoomId().equals(roomId)) {
                result.add(booking);
            }
        }
        result.sort(Comparator.comparing(Booking::getStart).thenComparingLong(Booking::getId));
        return result;
    }

    /** Every occurrence of one series, cancelled ones included, earliest first. */
    public List<Booking> seriesOccurrences(String seriesId) {
        List<Booking> result = new ArrayList<>();
        if (seriesId == null) {
            return result;
        }
        for (Booking booking : bookings) {
            if (seriesId.equals(booking.getSeriesId())) {
                result.add(booking);
            }
        }
        result.sort(Comparator.comparing(Booking::getStart).thenComparingLong(Booking::getId));
        return result;
    }
}
