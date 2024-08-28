package me.hypericats.hyperionclientv3.enums;

public enum EspColorType {
    NEAR("Near"),
    STATIC("Static");
    private final String name;
    EspColorType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
