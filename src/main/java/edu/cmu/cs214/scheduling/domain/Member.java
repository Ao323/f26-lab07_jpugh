package edu.cmu.cs214.scheduling.domain;

/** An account that can hold bookings. */
public class Member {

    private final String id;
    private final String name;
    private final String email;
    private final MembershipTier tier;

    public Member(String id, String name, String email, MembershipTier tier) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("member id must not be blank");
        }
        this.id = id;
        this.name = name == null ? id : name;
        this.email = email == null ? id + "@rooms.example.edu" : email;
        this.tier = tier == null ? MembershipTier.BASIC : tier;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MembershipTier getTier() {
        return tier;
    }

    @Override
    public String toString() {
        return name + " (" + id + ", " + tier + ")";
    }
}
