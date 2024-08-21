package me.hypericats.hyperionclientv3.enums;

public enum EntityTargetType {
    SINGLE("Single"),
    MULTI("Multi");
    private final String name;
    EntityTargetType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
