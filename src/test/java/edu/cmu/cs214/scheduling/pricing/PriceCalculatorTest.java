package edu.cmu.cs214.scheduling.pricing;

import edu.cmu.cs214.scheduling.domain.Booking;
import edu.cmu.cs214.scheduling.domain.BookingType;
import edu.cmu.cs214.scheduling.domain.Member;
import edu.cmu.cs214.scheduling.domain.MembershipTier;
import edu.cmu.cs214.scheduling.domain.TimeSlot;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();

    private static Booking bookingFrom(LocalDateTime start, LocalDateTime end) {
        return new Booking(1, "W-101", "m-1", new TimeSlot(start, end),
                BookingType.REGULAR, null, 0);
    }

    private static Member memberAt(MembershipTier tier) {
        return new Member("m-1", "Ada", "ada@rooms.example.edu", tier);
    }

    @Test
    void oneWeekdayHourCostsTheBaseRate() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 5, 9, 0),
                LocalDateTime.of(2026, 10, 5, 10, 0));

        assertEquals(40.00, calculator.price(booking, memberAt(MembershipTier.BASIC)), 0.001);
    }

    @Test
    void weekendsCarryASurcharge() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 10, 9, 0),
                LocalDateTime.of(2026, 10, 10, 10, 0));

        assertEquals(50.00, calculator.price(booking, memberAt(MembershipTier.BASIC)), 0.001);
    }

    @Test
    void threeHoursOrMoreTakeTheLongBookingDiscount() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 5, 9, 0),
                LocalDateTime.of(2026, 10, 5, 12, 0));

        assertEquals(108.00, calculator.price(booking, memberAt(MembershipTier.BASIC)), 0.001);
    }

    @Test
    void memberTierComesOffTheTop() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 5, 9, 0),
                LocalDateTime.of(2026, 10, 5, 10, 0));

        assertEquals(38.00, calculator.price(booking, memberAt(MembershipTier.PLUS)), 0.001);
        assertEquals(34.00, calculator.price(booking, memberAt(MembershipTier.PREMIER)), 0.001);
    }

    @Test
    void everyRuleAppliesInOrder() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 10, 9, 0),
                LocalDateTime.of(2026, 10, 10, 12, 0));

        assertEquals(114.75, calculator.price(booking, memberAt(MembershipTier.PREMIER)), 0.001);
    }

    @Test
    void aBookingWithNoMemberPaysNoTierDiscount() {
        Booking booking = bookingFrom(LocalDateTime.of(2026, 10, 5, 9, 0),
                LocalDateTime.of(2026, 10, 5, 10, 0));

        assertEquals(40.00, calculator.price(booking, null), 0.001);
    }
}
