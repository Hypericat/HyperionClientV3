package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.*;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import org.joml.Math;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class ModuleEditScreen extends Screen {
    private HyperionClientV3Screen parent;
    private Module module;
    protected NumberWidget focusedNumberWidget;
    private KeybindWidget keybindWidget;
    private boolean mouseDraggingToggle = false;
    private List<ModuleOptionsWidget> widgets = new ArrayList<>();
    public static int orangeColor = ColorHelper.Argb.getArgb(255, 252, 111, 3);
    public static int backgroundColor = ColorHelper.Argb.getArgb(95, 170, 255, 255);
    public ModuleEditScreen(Module module, HyperionClientV3Screen parent) {
        super(Text.of(module.getName() + " Edit Screen"));
        this.parent = parent;
        this.module = module;
        keybindWidget = new KeybindWidget(this.module);
        widgets.add(keybindWidget);
        for (ModuleOption<?> option : module.getOptions().getAllOptions()) {
            if (option instanceof BooleanOption) {
                widgets.add(new ToggleOptionsWidget((BooleanOption) option));
                continue;
            }
            if (option instanceof NumberOption<?>) {
                widgets.add(new NumberWidget((NumberOption<?>) option, this));
                continue;
            }
            if (option instanceof SliderOption<?>) {
                widgets.add(new SliderOptionsWidget((SliderOption<?>) option));
                continue;
            }
            if (option instanceof EnumStringOption<?>) {
                widgets.add(new MenuOptionWidget((EnumStringOption<?>) option));
                continue;
            }
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return false;
        if (keybindWidget.onKeyInput(keyCode, scanCode, modifiers)) return true;
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            parent.setScreen();
            return true;
        }
        if (keyCode == InputUtil.GLFW_KEY_BACKSPACE) {
            if (this.focusedNumberWidget != null) this.focusedNumberWidget.onBackSpace(modifiers);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MinecraftClient client = MinecraftClient.getInstance();

        float xPadding = 0.025f;
        float yPadding = 0.1f;
        float originX = xPadding * this.width;
        float originY = yPadding * this.height;
        float borderX = this.width - originX;
        float borderY = this.height - originY;

        Vector2f origin = new Vector2f(originX, originY);
        Vector2f border = new Vector2f(borderX, borderY);

        renderStaticElements(context, origin, border, client);
        renderOptions(context, origin, border, client);
    }
    private void renderStaticElements(DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        int backgroundA = ColorHelper.Argb.getArgb(200, 85, 128, 128);
        RenderUtil.fillWithBorder(context, (int) origin.getX(), (int) origin.getY(), (int) border.getX(), (int) border.getY(), backgroundColor, backgroundA, 4);

        float titleScale = 4f;
        String text = module.getName();
        // Calculate the X and Y positions before scaling
        int textWidth = client.textRenderer.getWidth(text);
        int x = (int) ((this.width / 2) / titleScale - textWidth / 2);
        //*0.75f causes a little bit of overlap with the fill
        int y = (int) ((origin.getY() / titleScale) - client.textRenderer.fontHeight * 0.75f);
        // Scale the matrix
        context.getMatrices().scale(titleScale, titleScale, titleScale);
        context.drawText(client.textRenderer, text, x, y, ColorUtils.getRgbColor(), true);
        context.getMatrices().scale(1 / titleScale, 1 / titleScale, 1 / titleScale);
    }
    private void renderOptions(DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        int x = this.width / 2;
        int spacing = 25;
        int y = (int) (origin.getY() + 20);
        int xOffset = 0;
        int xModifier = -1;
        int biggestWidth = 0;
        for (ModuleOptionsWidget widget : widgets) {
            widget.updatePos(x + xOffset * xModifier, y, context, origin, border, client);
            if (widget.getMaxWidth() > biggestWidth) biggestWidth = widget.getMaxWidth();
            if (y + widget.getHeight() > border.getY()) {
                if (xModifier > 0) {
                    xModifier = -1;
                } else {
                    xModifier = 1;
                    xOffset += biggestWidth + spacing;
                }
                biggestWidth = 0;
                y = (int) (origin.getY() + 20);
                widget.updatePos(x + xOffset * xModifier, y, context, origin, border, client);
            }
            widget.render(x + xOffset * xModifier, y, context, origin, border, client);
            y += widget.getHeight() + spacing;
        }
    }
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.focusedNumberWidget != null) {
            this.focusedNumberWidget.onCharTyped(chr, modifiers);
            return true;
        }
        return false;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        mouseDraggingToggle = true;
        this.focusedNumberWidget = null;
        for (ModuleOptionsWidget widget : widgets) {
            if (RenderUtil.isBetween((int) mouseX, (int) mouseY, (int) (widget.getX() - widget.getWidth() / 2d), widget.getY(), (int) (widget.getX() + widget.getWidth() / 2d), widget.getY() + widget.getHeight())) {
                widget.click(mouseX, mouseY, button);
            }
            //if (widget.getX() - widget.getWidth() / 2d < mouseX && widget.getX() + widget.getHeight() - widget.getWidth() / 2d > mouseX && widget.getY() < mouseY && widget.getY() + widget.getHeight() > mouseY) {
            //    widget.click(mouseX, mouseY, button);
            //}
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (ModuleOptionsWidget widget : widgets) {
            if (!(widget instanceof SliderOptionsWidget)) continue;
            ((SliderOptionsWidget) widget).onRelease();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!mouseDraggingToggle) return false;
        for (ModuleOptionsWidget widget : widgets) {
            if (!(widget instanceof SliderOptionsWidget)) continue;
            if (RenderUtil.isBetween((int) mouseX, (int) mouseY, (int) (widget.getX() - widget.getWidth() / 2d), widget.getY(), (int) (widget.getX() + widget.getWidth() / 2d), widget.getY() + widget.getHeight())) {
                widget.click(mouseX, mouseY, button);
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

}
