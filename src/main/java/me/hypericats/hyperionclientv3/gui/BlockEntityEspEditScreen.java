package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.mixinInterface.IScreen;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import me.hypericats.hyperionclientv3.util.BlockEspTarget;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockEntityEspEditScreen extends Screen {
    private Screen parent;
    private HashMap<Block, BlockEspTarget> renderMap;

    public static final int startY = 125;
    private final int xSide = 100;

    public BlockEntityEspEditScreen(Screen parent, HashMap<Block, BlockEspTarget> renderMap) {
        super(Text.of("Block Entity ESP Edit Screen"));
        this.parent = parent;
        this.renderMap = renderMap;
        this.client = MinecraftClient.getInstance();
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
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

        for (Drawable drawable : ((IScreen) this).getDrawables()) {
            drawable.render(context, mouseX, mouseY, delta);
        }

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
        TextFieldWidget addField = new TextFieldWidget(this.textRenderer, this.client.getWindow().getScaledWidth() / 2 - 150 / 2, startY + 7, 150, client.textRenderer.fontHeight * 2, Text.empty());
        ButtonWidget add = ButtonWidget.builder(Text.of("Add Block"), button -> {
            String field = addField.getText().toLowerCase();
            if (!field.startsWith("minecraft:")) field = "minecraft:" + field;
            Identifier identifier = Identifier.of(field);
            Block block = Registries.BLOCK.get(identifier);
            if (renderMap.containsKey(block)) {
                ChatUtils.sendError("Block ESP already contains &&a" + block.getName());
                return;
            }
            renderMap.put(block, new BlockEspTarget.builder(block).build());
            initWidgets();
        }).position(this.client.getWindow().getScaledWidth() / 2 - 150 / 2, startY + 12 + addField.getHeight()).build();
        this.addDrawableChild(addField);
        this.addDrawableChild(add);

        int x = xSide + 20;
        int xSpacing = 20;
        int y = startY + 75;
        int ySpacing;
        for (BlockEspTarget target : renderMap.values()) {
            Block block = target.getBlockType();
            String name = block.getName().getString();
            TextWidget widget = new TextWidget(x, y, textRenderer.getWidth(name), textRenderer.fontHeight, Text.of(name), this.textRenderer);
            ButtonWidget edit = ButtonWidget.builder(Text.of("Edit"), button -> this.client.setScreen(new BlockEditScreen(this, target))).position(x, y + widget.getHeight() + 5).width(70).build();
            ButtonWidget delete = ButtonWidget.builder(Text.literal("Delete").styled(style -> style.withColor(Formatting.RED)), button -> {
                if (!renderMap.containsKey(target.getBlockType())) return;
                renderMap.remove(target.getBlockType());
                initWidgets();
            }).position(x + edit.getWidth() + 10, y + widget.getHeight() + 5).width(70).build();
            x += Math.max(widget.getWidth(), (edit.getWidth() + 10 + delete.getWidth())) + xSpacing;
            this.addDrawableChild(widget);
            this.addDrawableChild(edit);
            this.addDrawableChild(delete);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            client.setScreen(parent);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
