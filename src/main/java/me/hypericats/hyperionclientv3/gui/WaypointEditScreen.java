package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class WaypointEditScreen extends Screen {
    private Screen parent;
    private List<WaypointWidget> waypointWidgets;
    public static final int startY = 125;
    public static final int ySpacing = 30;
    private int xSide = 1;
    public WaypointEditScreen(Screen parent) {
        super(Text.of("Waypoint Edit Screen"));
        this.parent = parent;
        initWidgets();
    }
    public Drawable addDrawableChild(Drawable drawable) {
        return this.addDrawable(drawable);
    }
    public <T extends Element & Drawable & Selectable> T addChild(T drawableElement) {
        return this.addDrawableChild(drawableElement);
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        int xOffset = 18;
        int yOffset = 30;
        RenderUtil.fillWithBorder(context, xSide - xOffset, startY - yOffset, MinecraftClient.getInstance().getWindow().getScaledWidth() - xSide + xOffset, MinecraftClient.getInstance().getWindow().getHeight(), -1778384896, -1, 3, true);

        for (WaypointWidget widget : waypointWidgets) {
            widget.render(context, mouseX, mouseY, delta);
        }
        drawTitle(startY - yOffset, context);

        super.render(context, mouseX, mouseY, delta);
    }
    private void drawTitle(int y, DrawContext context) {
        float scale = 4f;
        String text = "Waypoints";
        int textWidth = textRenderer.getWidth(text);
        int x = (int) ((this.width / 2) / scale - textWidth / 2);
        y = (int) ((y / scale) - textRenderer.fontHeight);
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawText(textRenderer, text, x, y, ColorUtils.getRgbColor(), true);

        context.getMatrices().pop();
    }
    public void initWidgets() {
        waypointWidgets = new ArrayList<>();
        this.clearChildren();
        xSide = -1;
        int y = startY;
        for (Waypoint waypoint : WaypointHandler.getWaypointLibrary()) {
            WaypointWidget widget = new WaypointWidget(waypoint, 0, y, this);
            widget.addChildren();
            if (xSide == -1) xSide = widget.getWidth();

            y += widget.getHeight() + ySpacing;
            waypointWidgets.add(widget);
        }
        xSide = MinecraftClient.getInstance().getWindow().getScaledWidth() / 2 - xSide / 2;
        for (WaypointWidget widget : waypointWidgets) {
            widget.setX(xSide);
        }
    }














}
