package me.hypericats.hyperionclientv3.enums;

public enum FullBrightType {
    GAMMA("Gamma"),
    NIGHTVISION("NightVision");
    private final String name;
    FullBrightType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
