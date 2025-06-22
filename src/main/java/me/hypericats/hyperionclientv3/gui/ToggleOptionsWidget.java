package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

public class ToggleOptionsWidget extends ModuleOptionsWidget {
    public ToggleOptionsWidget(BooleanOption option) {
        super(option);
    }
    private static final int enabledColor = ColorHelper.getArgb(255, 0, 255, 100);
    private static final int disabledColor = HyperionClientV3Screen.offColor;


    @Override
    public void render(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
        context.fill(x - this.getWidth() / 2, y, x + getWidth() / 2, y + getHeight(), ((BooleanOption) this.option).getValue() ? enabledColor : disabledColor);
        //RenderUtil.fillWithBorder(context, x - this.getWidth() / 2, y, x + getWidth() / 2, y + getHeight(), ((BooleanOption) this.option).getValue() ? enabledColor : disabledColor, ColorUtils.getRgbColor(255), 1);
        context.drawText(client.textRenderer, this.option.getName(), x - client.textRenderer.getWidth(this.option.getName()) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);
    }

    @Override
    public void updatePos(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {
        BooleanOption option = (BooleanOption) this.option;
        option.toggle();
        SoundHandler.playSound(SoundEvents.BLOCK_LEVER_CLICK, option.getValue() ? 1.5f : 0.5f);
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
        return Math.max(getWidth(), MinecraftClient.getInstance().textRenderer.getWidth(this.option.getName()));
    }
}
