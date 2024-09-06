package me.hypericats.hyperionclientv3.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;

public class CustomWidget extends ModuleOptionsWidget {
    private final ICustomWidget executor;
    private final String name;
    public CustomWidget(ICustomWidget executor, String name) {
        super(null);
        this.executor = executor;
        this.name = name;
    }

    @Override
    public void render(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
        context.fill(x - this.getWidth() / 2, y, x + getWidth() / 2, y + getHeight(), ColorHelper.Argb.getArgb(150, 255, 145, 0));

        context.drawText(client.textRenderer, name, x - client.textRenderer.getWidth(name) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);
    }

    @Override
    public void updatePos(int centerX, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = centerX;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {
        executor.execute(button);
        executor.playSound(button);
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
}
