package edu.cmu.cs214.scheduling.reporting;

/** How much of one room is spoken for. */
public record OccupancySummary(String roomId, String roomName, int activeBookings,
                               long bookedMinutes) {

    public double bookedHours() {
        return bookedMinutes / 60.0;
    }

    @Override
    public String toString() {
        return roomName + ": " + activeBookings + " bookings, " + bookedHours() + " h";
    }
}
