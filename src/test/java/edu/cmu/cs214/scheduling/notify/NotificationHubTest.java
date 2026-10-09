package edu.cmu.cs214.scheduling.notify;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

import edu.cmu.cs214.scheduling.domain.BookingRequest;
import edu.cmu.cs214.scheduling.domain.BookingStore;
import edu.cmu.cs214.scheduling.domain.Member;
import edu.cmu.cs214.scheduling.domain.MembershipTier;
import edu.cmu.cs214.scheduling.domain.Room;
import edu.cmu.cs214.scheduling.pricing.PriceCalculator;
import edu.cmu.cs214.scheduling.workflow.BookingWorkflow;

class NotificationHubTest {

    private static final LocalDateTime MON_9AM = LocalDateTime.of(2026, 10, 5, 9, 0);
    private static final LocalDateTime MON_10AM = LocalDateTime.of(2026, 10, 5, 10, 0);

    @Test
    void publishedMessageLandsInTheOutboxFullyRendered() {
        NotificationHub hub = new NotificationHub();

        hub.publish(new NotificationMessage("ada@rooms.example.edu", "Booking confirmed",
                "Room Willow Room from 2026-10-05T09:00 to 2026-10-05T10:00", MON_9AM));

        assertEquals(1, hub.getOutbox().size());
        assertEquals("To: ada@rooms.example.edu | Subject: Booking confirmed"
                + " | Room Willow Room from 2026-10-05T09:00 to 2026-10-05T10:00",
                hub.getOutbox().last());
    }

    @Test
    void hubDeliversToItsOneSubscriber() {
        NotificationHub hub = new NotificationHub();

        assertEquals(1, hub.subscriberCount());
    }

    @Test
    void subscribingAddsAnotherDeliveryTarget() {
        NotificationHub hub = new NotificationHub();

        hub.subscribe(rendered -> { });

        assertEquals(2, hub.subscriberCount());

        hub.publish(new NotificationMessage("ada@rooms.example.edu", "Booking confirmed",
                "Room Willow Room from 2026-10-05T09:00 to 2026-10-05T10:00", MON_9AM));

        assertEquals(1, hub.getOutbox().size());
    }

    @Test
    void factoryHandsBackTheSameInstance() {
        assertSame(NotifierFactory.getInstance(), NotifierFactory.getInstance());
    }

    @Test
    void aConfirmationFromTheWorkflowReachesTheOutbox() {
        BookingStore store = new BookingStore();
        store.addRoom(new Room("W-101", "Willow Room", 8));
        store.addMember(new Member("m-1", "Ada", "ada@rooms.example.edu", MembershipTier.BASIC));
        NotificationHub hub = new NotificationHub();
        BookingWorkflow workflow = new BookingWorkflow(store, new PriceCalculator(), hub);

        workflow.submit(BookingRequest.regular("W-101", "m-1", MON_9AM, MON_10AM, 4));

        assertEquals("To: ada@rooms.example.edu | Subject: Booking confirmed"
                + " | Room Willow Room from 2026-10-05T09:00 to 2026-10-05T10:00",
                hub.getOutbox().last());
    }
}
