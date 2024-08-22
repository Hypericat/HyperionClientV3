package me.hypericats.hyperionclientv3.util;

public class ColorUtils {
    public static int getRgbColor() {
        int r = (int) ((Math.sin(System.currentTimeMillis() / 1000.0) + 1) / 2 * 255);
        int g = (int) ((Math.sin(System.currentTimeMillis() / 2000.0) + 1) / 2 * 255);
        int b = (int) ((Math.sin(System.currentTimeMillis() / 3000.0) + 1) / 2 * 255);
        return (r << 16) | (g << 8) | b;
    }
    public static int setAlpha(int argb, int alpha) {
        return argb | alpha << 24;
    }
    public static int getRgbColor(int alpha) {
        return getRgbColor() | (alpha << 24);
    }
}
