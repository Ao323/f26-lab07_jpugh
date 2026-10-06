package edu.cmu.cs214.scheduling.domain;

/** Membership levels. The rate is the fraction taken off a booking price. */
public enum MembershipTier {

    BASIC(0.00),
    PLUS(0.05),
    PREMIER(0.15);

    private final double discountRate;

    MembershipTier(double discountRate) {
        this.discountRate = discountRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}
