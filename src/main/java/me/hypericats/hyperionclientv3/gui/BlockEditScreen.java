package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.util.BlockEspTarget;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.HashMap;

public class BlockEditScreen extends Screen {
    private Screen parent;
    private final BlockEspTarget target;

    public static final int startY = 125;
    private final int xSide = 100;

    public BlockEditScreen(Screen parent, BlockEspTarget target) {
        super(Text.of(target.getBlockType().getName().getString() + " Edit Screen"));
        this.parent = parent;
        this.target = target;
        this.client = MinecraftClient.getInstance();
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


        drawTitle(startY - yOffset, context);
    }
    private void drawTitle(int y, DrawContext context) {
        float scale = 4f;
        String text = "Block Entity ESP";
        int textWidth = textRenderer.getWidth(text);
        int x = (int) ((this.width / 2) / scale - textWidth / 2);
        y = (int) ((y / scale) - textRenderer.fontHeight);
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawText(textRenderer, text, x, y, ColorUtils.getRgbColor(), true);

        context.getMatrices().pop();
    }

    public void initWidgets() {
        this.clearChildren();

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            client.setScreen(parent);
            return true;
        }
        return false;
    }
}
