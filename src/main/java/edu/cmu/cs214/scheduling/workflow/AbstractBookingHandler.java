package edu.cmu.cs214.scheduling.workflow;

import edu.cmu.cs214.scheduling.domain.BookingStore;
import edu.cmu.cs214.scheduling.notify.NotificationHub;
import edu.cmu.cs214.scheduling.pricing.PriceCalculator;

abstract class AbstractBookingHandler implements BookingTypeHandler {

    protected static final String FACILITIES_CONTACT = "facilities@rooms.example.edu";

    protected final BookingStore store;
    protected final PriceCalculator calculator;
    protected final NotificationHub hub;

    protected AbstractBookingHandler(BookingStore store, PriceCalculator calculator,
                                     NotificationHub hub) {
        this.store = store;
        this.calculator = calculator;
        this.hub = hub;
    }
}
