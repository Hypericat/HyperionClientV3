package me.hypericats.hyperionclientv3;

public enum HackType {
    MOVEMENT("Movement"),
    COMBAT("Combat"),
    RENDER("Render"),
    ESP("Esp"),
    UTIL("Utility"),
    OTHER("Other"),
    UNFINISHED("Unfinished");

    private final String name;
    HackType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
