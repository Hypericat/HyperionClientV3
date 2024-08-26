package me.hypericats.hyperionclientv3.enums;

public enum NoFallSafety {
    SAFE("SAFE"),
    SAFEST("SAFEST");
    private final String name;
    NoFallSafety(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
