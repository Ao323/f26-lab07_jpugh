package edu.cmu.cs214.scheduling.reporting;

/** What a set of bookings is worth. */
public record RevenueSummary(int chargeableBookings, double total) {

    public double average() {
        return chargeableBookings == 0 ? 0.0 : total / chargeableBookings;
    }

    @Override
    public String toString() {
        return chargeableBookings + " chargeable bookings, $" + total;
    }
}
