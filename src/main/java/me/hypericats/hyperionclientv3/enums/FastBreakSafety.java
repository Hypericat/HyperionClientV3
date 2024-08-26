package me.hypericats.hyperionclientv3.enums;

public enum FastBreakSafety {
    SAFE("SAFE"),
    OBVIOUS("OBVIOUS");
    private final String name;
    FastBreakSafety(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
