package me.hypericats.hyperionclientv3.util;

import net.minecraft.client.gui.DrawContext;

public class RenderUtil {
    public static void fillWithBorder(DrawContext context, int x1, int y1, int x2, int y2, int innerColor, int borderColor, int borderThickness) {
        fillWithBorder(context, x1, y1, x2, y2, innerColor, borderColor, borderThickness, false);
    }
    public static void fillWithBorder(DrawContext context, int x1, int y1, int x2, int y2, int innerColor, int borderColor, int borderThickness, boolean ignoreBottom) {
        context.fill(x1, y1, x2, y2, innerColor);

        context.fill(x1, y1, x2, y1 + borderThickness, borderColor);
        context.fill(x2 - borderThickness, y2 - borderThickness, x2, y1 + borderThickness, borderColor);
        context.fill(x1, y2 - borderThickness, x1 + borderThickness, y1 + borderThickness, borderColor);

        if (!ignoreBottom)
            context.fill(x1, y2 - borderThickness, x2, y2, borderColor);
    }

    public static boolean isBetween(int cordX, int cordY, int x1, int y1, int x2, int y2) {
        int i;
        if (x1 < x2) {
            i = x1;
            x1 = x2;
            x2 = i;
        }
        if (y1 < y2) {
            i = y1;
            y1 = y2;
            y2 = i;
        }
        return cordX < x1 && cordX > x2 && cordY < y1 && cordY > y2;
    }

}
