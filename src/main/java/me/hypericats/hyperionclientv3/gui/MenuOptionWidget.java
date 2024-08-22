package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuOptionWidget extends ModuleOptionsWidget {
    public MenuOptionWidget(EnumStringOption<?> option) {
        super(option);
    }
    private int height = 0;
    private int width = 0;
    private int heightPerOption = 15;
    private int sideSpacing = 5;
    private Vector2d clickPos;

    @Override
    public void render(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        EnumStringOption<?> option = (EnumStringOption<?>) this.option;
        Class<?> enumClass = option.getValue().getClass();
        Enum<?>[] i = (Enum<?>[]) enumClass.getEnumConstants();
        List<Enum<?>> enums = (List.of(i));

        updatePos(x, y, context, origin, border, client, enums);

        int yOffset = y;
        int backgroundColorEnabled = ColorHelper.Argb.getArgb(120, 35, 255, 35);
        int backgroundColorDisabled = HyperionClientV3Screen.offColor;

        context.drawText(client.textRenderer, option.getName(), x - client.textRenderer.getWidth(option.getName()) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);
        for (Enum<?> e : enums) {
            //RenderUtil.fillWithBorder(context, x - this.getWidth() / 2, yOffset, x + this.getWidth() / 2, yOffset + heightPerOption, e == option.getValue() ? backgroundColorEnabled : backgroundColorDisabled, ColorUtils.getRgbColor(255), 1);
            context.fill(x - this.getWidth() / 2, yOffset, x + this.getWidth() / 2, yOffset + heightPerOption, e == option.getValue() ? backgroundColorEnabled : backgroundColorDisabled);
            if (clickPos != null) {
                if (RenderUtil.isBetween((int) clickPos.x, (int) clickPos.y, x - this.getWidth() / 2, yOffset, x + this.getWidth() / 2, yOffset + heightPerOption)) {
                    option.setValue(e);
                    clickPos = null;
                }
            }
            context.drawText(client.textRenderer, e.toString(), x - client.textRenderer.getWidth(e.toString()) / 2, yOffset + (heightPerOption - client.textRenderer.fontHeight) / 2, Colors.BLACK, false);
            yOffset += heightPerOption;
        }
    }

    @Override
    public void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
        EnumStringOption<?> option = (EnumStringOption<?>) this.option;
        Class<?> enumClass = option.getValue().getClass();
        Enum<?>[] i = (Enum<?>[]) enumClass.getEnumConstants();
        List<Enum<?>> enums = (List.of(i));
        this.height = enums.size() * heightPerOption;
        int longestWidth = 0;
        for (Enum<?> e : enums) {
            String text = e.toString();
            int width = client.textRenderer.getWidth(text) + sideSpacing * 2;
            if (width > longestWidth) longestWidth = width;
        }
        width = longestWidth;
    }
    private void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client, List<Enum<?>> enums) {
        this.x = x;
        this.y = y;
        this.height = enums.size() * heightPerOption;
        int longestWidth = 0;
        for (Enum<?> e : enums) {
            String text = e.toString();
            int width = client.textRenderer.getWidth(text) + sideSpacing * 2;
            if (width > longestWidth) longestWidth = width;
        }
        width = longestWidth;
    }
    @Override
    public void click(double x, double y, int button) {
        if (button != 0) return;
        clickPos = new Vector2d(x, y);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
