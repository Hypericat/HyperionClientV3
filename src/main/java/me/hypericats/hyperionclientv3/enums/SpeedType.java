package me.hypericats.hyperionclientv3.enums;

public enum SpeedType {
    CONTROL("Control"),
    VELOCITY("Velocity"),
    JUMP("Jump"),
    BHOP("BHop");
    private final String name;
    SpeedType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
