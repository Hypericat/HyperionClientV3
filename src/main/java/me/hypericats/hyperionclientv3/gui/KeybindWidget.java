package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

public class KeybindWidget extends ModuleOptionsWidget {
    private Module module;
    private boolean isActive;
    private String name;
    public KeybindWidget(Module module) {
        super(null);
        this.name = "Keyboard Input Toggle";
        this.module = module;
    }
    private static final int enabledColor = ColorHelper.Argb.getArgb(255, 0, 255, 100);
    private static final int disabledColor = HyperionClientV3Screen.offColor;


    @Override
    public void render(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
        context.fill(x - this.getWidth() / 2, y, x + getWidth() / 2, y + getHeight(), isActive ? enabledColor : disabledColor);
        context.drawText(client.textRenderer, name, x - client.textRenderer.getWidth(name) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);
        context.drawText(client.textRenderer, "Key", x - client.textRenderer.getWidth("Key") / 2, y + client.textRenderer.fontHeight, HyperionClientV3Screen.orangeColor, false);
        String key;
        if (module.getKey() == null) {
            key = "Null";
        } else {
            key = module.getKey().getTranslationKey();
            if ((Character.isDefined(module.getKey().getCode()))) {
                char c = (char) module.getKey().getCode();
                key = String.valueOf(c);
            }
        }
        context.drawText(client.textRenderer, key, x - client.textRenderer.getWidth(key) / 2, (int) (y + client.textRenderer.fontHeight * 2.5f), HyperionClientV3Screen.orangeColor, false);

    }

    @Override
    public void updatePos(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {
        if (!isActive) {
            isActive = true;
            SoundHandler.playSound(SoundEvents.BLOCK_LEVER_CLICK, 1.5f);
        } else {
            isActive = false;
            module.setKey(null);
            SoundHandler.playSound(SoundEvents.BLOCK_LEVER_CLICK, 0.5f);
        }
    }

    @Override
    public int getWidth() {
        return 35;
    }

    @Override
    public int getHeight() {
        return 35;
    }

    @Override
    public int getMaxWidth() {
        return Math.max(getWidth(), MinecraftClient.getInstance().textRenderer.getWidth(name));
    }
    //return true if should block key
    public boolean onKeyInput(int keyCode, int scanCode, int modifiers) {
        if (!this.isActive) return false;
        module.setKey(InputUtil.fromKeyCode(keyCode, scanCode));
        isActive = false;
        SoundHandler.playSound(SoundEvents.BLOCK_LEVER_CLICK, 0.5f);
        return true;
    }
}
