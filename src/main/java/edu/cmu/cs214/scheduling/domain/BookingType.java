package edu.cmu.cs214.scheduling.domain;

/** The kinds of booking the scheduler accepts. */
public enum BookingType {

    /** A single reservation held by one member. */
    REGULAR,

    /** A weekly series held by one member. */
    RECURRING,

    /** An administrative hold. No member, no charge. */
    BLOCKED
}
