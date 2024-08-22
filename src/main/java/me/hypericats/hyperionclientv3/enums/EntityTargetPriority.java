package me.hypericats.hyperionclientv3.enums;

public enum EntityTargetPriority {
    CLOSEST("Closest"),
    FURTHEST("Furthest"),
    PLAYER("Player"),
    HOSTILE("Hostile"),
    PASSIVE("Passive");
    private final String name;
    EntityTargetPriority(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
