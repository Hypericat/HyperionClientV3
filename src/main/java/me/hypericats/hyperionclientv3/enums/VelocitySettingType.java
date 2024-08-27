package me.hypericats.hyperionclientv3.enums;

public enum VelocitySettingType {
    MULTIPLIER("Multiplier"),
    INDIVIDUAL("Individual");
    private final String name;
    VelocitySettingType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
