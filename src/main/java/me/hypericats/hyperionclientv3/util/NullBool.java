package me.hypericats.hyperionclientv3.util;

public enum NullBool {
    NULL,
    TRUE,
    FALSE;

    public boolean toBool() {
        return this == TRUE;
    }
}
