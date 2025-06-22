package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WaypointWidget implements Widget {
    private Waypoint waypoint;
    List<ClickableWidget> widgets;
    private List<Pair<ClickableWidget, String>> fieldNames;
    private ButtonWidget colorButton;
    private final int xSpacing = 15;
    private TextRenderer textRenderer;
    private int x;
    private int y;
    private int width;
    private int height;
    private WaypointEditScreen parentScreen;

    public WaypointWidget(Waypoint waypoint, int x, int y, WaypointEditScreen parentScreen) {
        this.waypoint = waypoint;
        this.x = x;
        this.y = y;
        this.parentScreen = parentScreen;
        initWidgets(waypoint);
    }
    public Waypoint getWaypoint() {
        return waypoint;
    }
    @Override
    public void setX(int x) {
        this.x = x;
        updateWidgetPos();
    }

    @Override
    public void setY(int y) {
        this.y = y;
        updateWidgetPos();
    }
    public void setPos(int x, int y) {
        this.x = x;
        this.y =y;
        updateWidgetPos();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public ScreenRect getNavigationFocus() {
        return Widget.super.getNavigationFocus();
    }

    @Override
    public void setPosition(int x, int y) {
        Widget.super.setPosition(x, y);
    }
    private void initWidgets(Waypoint waypoint) {
        widgets = new ArrayList<>();
        fieldNames = new ArrayList<>();
        this.textRenderer = MinecraftClient.getInstance().textRenderer;

        initTextField(125, waypoint.getName(), waypoint::setName, "Display Name");

        ButtonWidget setPosHere = ButtonWidget.builder(Text.of("Set Here"), s -> waypoint.setPos(MinecraftClient.getInstance().player == null ? Vec3d.ZERO : MinecraftClient.getInstance().player.getBoundingBox().getCenter())).build();
        colorButton = setPosHere;
        setPosHere.setWidth(textRenderer.getWidth(setPosHere.getMessage().getString()) + 10);
        widgets.add(setPosHere);

        initTextField(45, String.valueOf(waypoint.getX()), s -> setPos(s, Direction.Axis.X), "X Pos");
        initTextField(45, String.valueOf(waypoint.getY()), s -> setPos(s, Direction.Axis.Y), "Y Pos");
        initTextField(45, String.valueOf(waypoint.getZ()), s -> setPos(s, Direction.Axis.Z), "Z Pos");
        initTextField(30, String.valueOf(ColorHelper.getAlpha(waypoint.getColor())), s -> setColor(s, 0), "Alpha");
        initTextField(30, String.valueOf(ColorHelper.getRed(waypoint.getColor())), s -> setColor(s, 1), "Red");
        initTextField(30, String.valueOf(ColorHelper.getGreen(waypoint.getColor())), s -> setColor(s, 2), "Green");
        initTextField(30, String.valueOf(ColorHelper.getBlue(waypoint.getColor())), s -> setColor(s, 3), "Blue");
        initTextField(50, String.valueOf(waypoint.getMaxRange()), this::setMaxRange, "Max Range");
        initTextField(50, String.valueOf(waypoint.getMinRange()), this::setMinRange, "Min Range");
        initTextField(50, String.valueOf(waypoint.getScale()), this::setScale, "Scale");

        ButtonWidget visible = ButtonWidget.builder(Text.literal("Visible").styled(style -> style.withColor(getColor(waypoint.isVisible()))), button -> {
            waypoint.setVisible(!waypoint.isVisible());
            button.setMessage(Text.literal("Visible").styled(style -> style.withColor(getColor(waypoint.isVisible()))));
        }).build();
        visible.setWidth(textRenderer.getWidth(visible.getMessage().getString()) + 10);
        widgets.add(visible);

        ButtonWidget renderBackground = ButtonWidget.builder(Text.literal("Show Background").styled(style -> style.withColor(getColor(waypoint.isRenderBackground()))), button -> {
            waypoint.setRenderBackground(!waypoint.isRenderBackground());
            button.setMessage(Text.literal("Show Background").styled(style -> style.withColor(getColor(waypoint.isRenderBackground()))));
        }).build();
        renderBackground.setWidth(textRenderer.getWidth(renderBackground.getMessage().getString()) + 10);
        widgets.add(renderBackground);

        ButtonWidget scaleScale = ButtonWidget.builder(Text.literal("Distance Scale").styled(style -> style.withColor(getColor(waypoint.isScaleScale()))), button -> {
            waypoint.setScaleScale(!waypoint.isScaleScale());
            button.setMessage(Text.literal("Distance Scale").styled(style -> style.withColor(getColor(waypoint.isScaleScale()))));
        }).build();
        scaleScale.setWidth(textRenderer.getWidth(scaleScale.getMessage().getString()) + 10);
        widgets.add(scaleScale);

        ButtonWidget delete = ButtonWidget.builder(Text.literal("Delete").styled(style -> style.withColor(Formatting.WHITE)), button -> {
            WaypointHandler.removeFromLibrary(waypoint);
            parentScreen.initWidgets();
        }).build();
        delete.setWidth(textRenderer.getWidth(delete.getMessage().getString()) + 10);
        widgets.add(delete);


        // Update positions and dimensions
        this.updateColor();
        this.calcDimensions();
    }
    private void initTextField(int width, String text, Consumer<String> changedListener, String displayName) {
        TextFieldWidget field = new TextFieldWidget(textRenderer, 0, 0, width, textRenderer.fontHeight * 2, null);
        field.setChangedListener(changedListener);
        field.setText(text);
        field.setCursor(0, false);
        widgets.add(field);
        fieldNames.add(new Pair<>(field, displayName));
    }
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ClickableWidget widget;
        String string;
        for (Pair<ClickableWidget, String> pair : fieldNames) {
            widget = pair.getLeft();
            string = pair.getRight();
            if (widget == null || string.isEmpty()) continue;
            context.drawText(textRenderer, string, widget.getX() + widget.getWidth() / 2 - textRenderer.getWidth(string) / 2, widget.getY() - 12, -1, false);
        }
    }

    private void calcDimensions() {
        height = 0;
        for (Widget widget : widgets) {
            if (widget.getHeight() > height) height = widget.getHeight();
        }
        width = -xSpacing;
        for (ClickableWidget clickableWidget : widgets) {
            width += clickableWidget.getWidth() + xSpacing;
        }
        width = Math.max(0, width);

    }
    public Formatting getColor(boolean state) {
        if (state) return Formatting.GREEN;
        return Formatting.RED;
    }
    private void updateWidgetPos() {
        int x = this.getX();
        for (ClickableWidget clickableWidget : widgets) {
            clickableWidget.setX(x);
            clickableWidget.setY(this.getY());
            x += clickableWidget.getWidth() + xSpacing;
        }
    }
    private void setMaxRange(String strValue) {
        double value;
        try {
            value = Double.parseDouble(strValue);
        } catch (Exception e) {
            return;
        }
        waypoint.setMaxRange(value);
    }
    private void setMinRange(String strValue) {
        double value;
        try {
            value = Double.parseDouble(strValue);
        } catch (Exception e) {
            return;
        }
        waypoint.setMinRange(value);
    }
    private void setScale(String strValue) {
        float value;
        try {
            value = Float.parseFloat(strValue);
        } catch (Exception e) {
            return;
        }
        waypoint.setScale(value);
    }
    private void setPos(String strPos, Direction.Axis axis) {
        double pos;
        try {
            pos = Double.parseDouble(strPos);
        } catch (Exception e) {
            return;
        }
        switch (axis) {
            case X -> waypoint.setX(pos);
            case Y -> waypoint.setY(pos);
            case Z -> waypoint.setZ(pos);
        }
    }
    private void setColor(String strColor, int index) {
        int color;
        try {
            color = Integer.parseInt(strColor);
        } catch (Exception e) {
            return;
        }
        int currentColor = waypoint.getColor();
        int a = ColorHelper.getAlpha(currentColor);
        int r = ColorHelper.getRed(currentColor);
        int g = ColorHelper.getGreen(currentColor);
        int b = ColorHelper.getBlue(currentColor);
        switch (index) {
            case (0) -> waypoint.setColor(ColorHelper.getArgb(color, r, g, b));
            case (1) -> waypoint.setColor(ColorHelper.getArgb(a, color, g, b));
            case (2) -> waypoint.setColor(ColorHelper.getArgb(a, r, color, b));
            case (3) -> waypoint.setColor(ColorHelper.getArgb(a, r, g, color));
        }
        updateColor();
    }
    private void updateColor() {
        int color = waypoint.getColorFullAlpha();
        for (ClickableWidget widget : widgets) {
            if (widget instanceof TextFieldWidget textWidget) {
                textWidget.setEditableColor(color);
                textWidget.setUneditableColor(color);
            }
        }
        if (colorButton != null) {
            colorButton.setMessage(Text.literal(colorButton.getMessage().getString()).styled(style -> style.withColor(color)));
        }

    }
    public void addChildren() {
        for (ClickableWidget clickableWidget : widgets) {
            parentScreen.addChild(clickableWidget);
        }
    }




    @Override
    public void forEachChild(Consumer<ClickableWidget> consumer) {
        return;
    }
}
