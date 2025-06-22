package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.NumberUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.StringHelper;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import org.joml.Math;

public class NumberWidget extends ModuleOptionsWidget {
    private String fieldString;
    private ModuleEditScreen parent;
    private boolean lastSyncStatus;
    private int width = 100;
    private int height = 22;
    private float textScale = 1.5f;
    public NumberWidget(NumberOption<?> option, ModuleEditScreen parent) {
        super(option);
        this.parent = parent;
        fieldString = option.getValue().toString();
        lastSyncStatus = true;
    }


    @Override
    public void render(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;

        int width = 2;
        int focusedColorSynced = ColorHelper.getArgb(255, 100, 255, 255);
        int focusedColorDesynced = ColorHelper.getArgb(255, 255, 100, 100);
        int unfocusedColorSynced = ColorHelper.getArgb(150, 100, 255, 255);
        int unfocusedColorDesynced = ColorHelper.getArgb(150, 255, 100, 100);
        context.drawText(client.textRenderer, option.getName(), x - client.textRenderer.getWidth(option.getName()) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);
        context.fill(x - this.getWidth() / 2, y - width + this.height, x + this.getWidth() / 2, y + width + this.getHeight(), parent.focusedNumberWidget == this ? lastSyncStatus ? focusedColorSynced : focusedColorDesynced : lastSyncStatus ? unfocusedColorSynced : unfocusedColorDesynced);
        RenderUtil.fillWithBorder(context, x - this.getWidth() / 2, y, x + this.getWidth() / 2, y + this.getHeight(), 0, ColorHelper.getArgb(30, 0, 0, 0), 1, true);
        // Calculate the X and Y positions before scaling
        int textWidth = client.textRenderer.getWidth(fieldString);
        int textX = (int) (x / textScale - textWidth / 2);
        int textY = (int) ((int) ((y + width) / textScale) + client.textRenderer.fontHeight / textScale / 1.5f);
        // Scale the matrix
        context.getMatrices().scale(textScale, textScale, textScale);
        context.drawText(client.textRenderer, fieldString, textX, textY, HyperionClientV3Screen.orangeColor, false);
        context.getMatrices().scale(1 / textScale, 1 / textScale, 1 / textScale);
    }

    @Override
    public void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {
        if (parent.focusedNumberWidget == this) return;
        parent.focusedNumberWidget = this;
        SoundHandler.playSound(SoundEvents.BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF, 2f, 1f);
    }

    @Override
    public int getWidth() {
        return width;
    }
    public void onBackSpace(int modifiers) {
        if (fieldString.isEmpty()) {
            updateStatus();
            return;
        }
        if (modifiers == 2) {
            fieldString = "";
            updateStatus();
            return;
        }
        fieldString = fieldString.substring(0, fieldString.length() - 1);
        lastSyncStatus = updateStatus();
    }
    public void onCharTyped(char chr, int modifiers) {
        if (!StringHelper.isValidChar(chr)) return;
        if (MinecraftClient.getInstance().textRenderer.getWidth(fieldString + chr) * textScale > this.getWidth()) return;

        fieldString += chr;
        lastSyncStatus = updateStatus();
    }
    public boolean updateStatus() {
        NumberOption<?> option = (NumberOption<?>) this.option;
        if (option.getValue() instanceof Double || option.getValue() instanceof Float) {
            try {
                Double d = Double.parseDouble(fieldString);
                option.setValue(d);
                return true;
            } catch (Exception ignore) {
                return false;
            }
        }
        if (option.getValue() instanceof Integer || option.getValue() instanceof Long || option.getValue() instanceof Short || option.getValue() instanceof Byte) {
            try {
                Long l = Long.parseLong(fieldString);
                option.setValue(l);
                return true;
            } catch (Exception ignore) {
                return false;
            }
        }
        return false;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
