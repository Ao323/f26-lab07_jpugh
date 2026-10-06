package edu.cmu.cs214.scheduling.reporting;

import edu.cmu.cs214.scheduling.domain.BookingRequest;
import edu.cmu.cs214.scheduling.domain.BookingStore;
import edu.cmu.cs214.scheduling.domain.Member;
import edu.cmu.cs214.scheduling.domain.MembershipTier;
import edu.cmu.cs214.scheduling.domain.Room;
import edu.cmu.cs214.scheduling.notify.NotificationHub;
import edu.cmu.cs214.scheduling.pricing.PriceCalculator;
import edu.cmu.cs214.scheduling.workflow.BookingWorkflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportServiceTest {

    private static final LocalDateTime MON_9AM = LocalDateTime.of(2026, 10, 5, 9, 0);
    private static final LocalDateTime MON_10AM = LocalDateTime.of(2026, 10, 5, 10, 0);
    private static final LocalDateTime MON_11AM = LocalDateTime.of(2026, 10, 5, 11, 0);
    private static final LocalDateTime MON_1PM = LocalDateTime.of(2026, 10, 5, 13, 0);

    private BookingStore store;
    private BookingWorkflow workflow;
    private ReportService reports;

    @BeforeEach
    void setUp() {
        store = new BookingStore();
        store.addRoom(new Room("W-101", "Willow Room", 8));
        store.addRoom(new Room("C-200", "Cedar Hall", 20));
        store.addMember(new Member("m-1", "Ada", "ada@rooms.example.edu", MembershipTier.BASIC));
        store.addMember(new Member("m-2", "Grace", "grace@rooms.example.edu",
                MembershipTier.BASIC));
        PriceCalculator calculator = new PriceCalculator();
        workflow = new BookingWorkflow(store, calculator, new NotificationHub());
        reports = new ReportService(store, calculator);
    }

    @Test
    void occupancyCountsLiveBookingsAndMinutes() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));
        workflow.submit(BookingRequest.regular("W-101", "m-2", MON_10AM, MON_11AM, 4));

        OccupancySummary summary = reports.occupancyFor("W-101");

        assertEquals("Willow Room", summary.roomName());
        assertEquals(2, summary.activeBookings());
        assertEquals(120, summary.bookedMinutes());
        assertEquals(2.0, summary.bookedHours(), 0.001);
    }

    @Test
    void occupancyDropsCancelledBookings() {
        long id = workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4))
                .getBooking().getId();
        workflow.cancel(id, false);

        assertEquals(0, reports.occupancyFor("W-101").activeBookings());
    }

    @Test
    void occupancyReportsEveryRegisteredRoom() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));

        assertEquals(2, reports.occupancyByRoom().size());
    }

    @Test
    void revenueSkipsBlockedSlots() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));
        workflow.submit(BookingRequest.blocked("C-200", MON_11AM, MON_1PM));

        RevenueSummary summary = reports.totalRevenue();

        assertEquals(1, summary.chargeableBookings());
        assertEquals(40.00, summary.total(), 0.001);
    }

    @Test
    void revenueForOneMemberIgnoresTheOthers() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));
        workflow.submit(BookingRequest.regular("C-200", "m-2", MON_9AM, MON_11AM, 4));

        assertEquals(40.00, reports.revenueForMember("m-1").total(), 0.001);
        assertEquals(80.00, reports.revenueForMember("m-2").total(), 0.001);
    }

    @Test
    void busiestRoomIsTheOneWithTheMostMinutes() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));
        workflow.submit(BookingRequest.regular("C-200", "m-2", MON_9AM, MON_1PM, 4));

        assertEquals("C-200", reports.busiestRoomId());
    }

    @Test
    void scheduleListsOneDayInOneRoom() {
        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));
        workflow.submit(BookingRequest.regular("W-101", "m-2",
                MON_9AM.plusWeeks(1), MON_10AM.plusWeeks(1), 4));

        assertEquals(1, reports.scheduleFor("W-101", LocalDate.of(2026, 10, 5)).size());
    }
}
