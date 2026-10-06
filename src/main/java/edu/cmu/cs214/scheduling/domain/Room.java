package edu.cmu.cs214.scheduling.domain;

/** A bookable room. */
public class Room {

    private final String id;
    private final String name;
    private final int capacity;

    public Room(String id, String name, int capacity) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("room id must not be blank");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("room capacity must be positive");
        }
        this.id = id;
        this.name = name == null ? id : name;
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name + " (" + id + ", seats " + capacity + ")";
    }
}
