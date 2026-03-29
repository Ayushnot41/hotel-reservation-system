package com.hotel.reservation.model.enums;

public enum RoomType {
    SINGLE("Single"),
    DOUBLE("Double"),
    SUITE("Suite"),
    DELUXE("Deluxe"),
    FAMILY("Family");

    private final String displayName;

    RoomType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
