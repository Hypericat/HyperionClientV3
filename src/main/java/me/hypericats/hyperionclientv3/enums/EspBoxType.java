package me.hypericats.hyperionclientv3.enums;

public enum EspBoxType {
    BOUNDINGBOX("BoundingBox"),
    SHAPE("Shape");
    private final String name;
    EspBoxType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
