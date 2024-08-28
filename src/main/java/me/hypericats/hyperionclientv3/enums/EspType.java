package me.hypericats.hyperionclientv3.enums;

public enum EspType {
    BOX("Box"),
    TRACER("Tracer"),
    BOXANDTRACER("BoxAndTracer");
    private final String name;
    EspType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
