package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.moduleOptions.ModuleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;

public abstract class ModuleOptionsWidget {
    protected int x;
    protected int y;
    protected ModuleOption<?> option;
    public ModuleOptionsWidget(ModuleOption<?> option) {
        this.option = option;
    }
    public abstract void render(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client);
    public abstract void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client);
    public abstract void click(double x, double y, int button);
    public abstract int getWidth();
    public int getMaxWidth() {
        return getWidth();
    };
    public abstract int getHeight();
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
