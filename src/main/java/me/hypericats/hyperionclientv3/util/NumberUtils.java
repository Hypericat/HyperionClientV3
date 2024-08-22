package me.hypericats.hyperionclientv3.util;

public class NumberUtils {
    public static boolean isDouble(String dub) {
        if (dub == null) return false;
        boolean first = true;
        int periodCounter = 0;
        if (dub.length() == 0) return false;
        for (char cha : dub.toCharArray()) {
            if (first && String.valueOf(cha).equals(".")) return false;
            if (!"-1234567890.".contains(String.valueOf(cha))) return false;
            if (!first && String.valueOf(cha).equals("-")) return false;
            if (String.valueOf(cha).equals(".")) periodCounter ++;
            first = false;
        }
        return periodCounter <= 1;
    }
    public static boolean isInt(String inte) {
        if (inte == null) return false;
        if (inte.length() == 0) return false;
        for (char cha : inte.toCharArray()) {
            if (!"1234567890".contains(String.valueOf(cha))) return false;
        }
        return true;
    }
}
