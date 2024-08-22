package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.moduleOptions.SliderOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;

public class SliderOptionsWidget extends ModuleOptionsWidget {
    public SliderOptionsWidget(SliderOption<?> option) {
        super(option);
    }


    @Override
    public void render(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
