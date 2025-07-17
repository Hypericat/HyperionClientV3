package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.auth.SessionHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.text.Text;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SessionScreen extends Screen {
    private final Screen parent;
    private static final int SPACING = 10;

    private String accessToken = MinecraftClient.getInstance().getSession().getAccessToken();
    private String userName = MinecraftClient.getInstance().getSession().getUsername();

    public SessionScreen(Screen parent) {
        super(Text.of("Session Screen"));
        this.parent = parent;
    }

    @Override
    public void close() {
        MinecraftClient.getInstance().currentScreen = parent;
    }

    public TextFieldWidget initTextFieldWidget(TextFieldWidget widget, Consumer<String> listener, String defaultValue, int length) {
        widget.setMaxLength(length);
        widget.setChangedListener(listener);
        widget.setText(defaultValue);
        return widget;
    }

    @Override
    protected void init() {
        int xCenter = this.width >> 1;
        AtomicInteger yPos = new AtomicInteger(this.height >> 1);
        List<Widget> widgets = new ArrayList<>();

        widgets.add(ButtonWidget.builder(Text.of("Copy Session"), button -> MinecraftClient.getInstance().keyboard.setClipboard(MinecraftClient.getInstance().getSession().getAccessToken())).size(135, 20).build());

        widgets.add(new TextWidget(135, 20, Text.of("Username"), MinecraftClient.getInstance().textRenderer));
        widgets.add(initTextFieldWidget(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 135, 20, Text.of("Username")), s -> userName = parseName(s), userName, 16));


        widgets.add(new TextWidget(135, 20, Text.of("Session ID"), MinecraftClient.getInstance().textRenderer));
        widgets.add(initTextFieldWidget(new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 135, 20, Text.of("Session ID")), s -> accessToken = s.trim(), accessToken, Integer.MAX_VALUE));

        widgets.add(ButtonWidget.builder(Text.of("Login"), button -> trySetSession()).size(135, 20).build());

        setHeight(widgets, yPos);
        widgets.forEach(w -> finalize(w, xCenter, yPos));
    }

    public void setHeight(List<Widget> widgets, AtomicInteger yPos) {
        widgets.forEach(w -> yPos.set(yPos.get() - ((w.getHeight() + SPACING) >> 1)));
        yPos.set(yPos.get() + (SPACING >> 1));
    }

    public void trySetSession() {
        if (userName == null || userName.isEmpty()) return;

        if (accessToken != null && !accessToken.isEmpty()) {
            SessionHandler.setSession(SessionHandler.getFromSessionID(userName, accessToken));
            return;
        }

        SessionHandler.setSession(SessionHandler.getCrackedSession(userName));
    }

    public String parseName(String name) {
        return name.trim().replaceAll("[^A-Za-z0-9_]", "");
    }

    public<T extends Drawable & Element & Selectable> void finalize(Widget widget, int xCenter, AtomicInteger yPos) {
        widget.setPosition(xCenter - (widget.getWidth() >> 1), yPos.get() - (widget.getHeight() >> 1));
        this.addDrawableChild((T) widget); // Probably fine
        yPos.set(yPos.get() + widget.getHeight() + SPACING);
    }
}
