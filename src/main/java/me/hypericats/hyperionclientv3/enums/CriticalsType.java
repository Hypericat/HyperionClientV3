package me.hypericats.hyperionclientv3.enums;

public enum CriticalsType {
    PACKET("Packet"),
    VELOCITY("Velocity");
    private final String name;
    CriticalsType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
