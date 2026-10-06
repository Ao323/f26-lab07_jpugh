package edu.cmu.cs214.scheduling.reporting;

import edu.cmu.cs214.scheduling.domain.Booking;
import edu.cmu.cs214.scheduling.domain.BookingStore;
import edu.cmu.cs214.scheduling.domain.BookingType;
import edu.cmu.cs214.scheduling.domain.Member;
import edu.cmu.cs214.scheduling.domain.Room;
import edu.cmu.cs214.scheduling.pricing.PriceCalculator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the store and reports on it. Blocked slots occupy a room but never
 * appear in revenue; cancelled bookings appear in neither.
 */
public class ReportService {

    private final BookingStore store;
    private final PriceCalculator calculator;

    public ReportService(BookingStore store, PriceCalculator calculator) {
        this.store = store;
        this.calculator = calculator;
    }

    /** One row per known room, in the order the rooms were registered. */
    public List<OccupancySummary> occupancyByRoom() {
        List<OccupancySummary> rows = new ArrayList<>();
        for (Room room : store.allRooms()) {
            rows.add(occupancyFor(room.getId()));
        }
        return rows;
    }

    public OccupancySummary occupancyFor(String roomId) {
        Room room = store.findRoom(roomId);
        String name = room == null ? roomId : room.getName();
        int count = 0;
        long minutes = 0;
        for (Booking booking : store.activeInRoom(roomId)) {
            count++;
            minutes += booking.getSlot().minutes();
        }
        return new OccupancySummary(roomId, name, count, minutes);
    }

    /** Live bookings in one room on one calendar day, earliest first. */
    public List<Booking> scheduleFor(String roomId, LocalDate day) {
        List<Booking> rows = new ArrayList<>();
        for (Booking booking : store.activeInRoom(roomId)) {
            if (booking.getStart().toLocalDate().equals(day)) {
                rows.add(booking);
            }
        }
        return rows;
    }

    /** Revenue across every live, chargeable booking in the store. */
    public RevenueSummary totalRevenue() {
        int count = 0;
        double total = 0.0;
        for (Booking booking : store.allBookings()) {
            if (!chargeable(booking)) {
                continue;
            }
            count++;
            total += calculator.price(booking, store.findMember(booking.getMemberId()));
        }
        return new RevenueSummary(count, round(total));
    }

    public RevenueSummary revenueForMember(String memberId) {
        Member member = store.findMember(memberId);
        int count = 0;
        double total = 0.0;
        for (Booking booking : store.allBookings()) {
            if (!chargeable(booking) || !booking.getMemberId().equals(memberId)) {
                continue;
            }
            count++;
            total += calculator.price(booking, member);
        }
        return new RevenueSummary(count, round(total));
    }

    /** The room with the most booked minutes, or null when nothing is booked. */
    public String busiestRoomId() {
        String busiest = null;
        long best = 0;
        for (OccupancySummary row : occupancyByRoom()) {
            if (row.bookedMinutes() > best) {
                best = row.bookedMinutes();
                busiest = row.roomId();
            }
        }
        return busiest;
    }

    private static boolean chargeable(Booking booking) {
        return !booking.isCancelled() && booking.getType() != BookingType.BLOCKED;
    }

    private static double round(double amount) {
        return Math.round(amount * 100.0) / 100.0;
    }
}
