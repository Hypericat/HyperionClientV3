package me.hypericats.hyperionclientv3.util;

import java.util.List;

public interface Condition <T> {
    public boolean apply(T... value);
}
