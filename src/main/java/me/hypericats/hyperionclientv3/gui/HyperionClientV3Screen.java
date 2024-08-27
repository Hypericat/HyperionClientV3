package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.KeyInputListener;
import me.hypericats.hyperionclientv3.events.eventData.KeyInputData;
import me.hypericats.hyperionclientv3.modules.InvWalk;
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
import net.minecraft.util.math.MathHelper;
import org.joml.Math;
import org.joml.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class HyperionClientV3Screen extends Screen {
    private String openKeyTranslationKey = "key.keyboard.right.shift";
    private String searchString = "";
    private List<Module> toRenderModules = new ArrayList<>();
    private Vector2d mouseClick;
    private int mouseButton = -1;
    private int currentBarSize = -1;
    public static int orangeColor = ColorHelper.Argb.getArgb(255, 252, 111, 3);
    public static int backgroundColor = ColorHelper.Argb.getArgb(95, 170, 255, 255);
    public static int offColor = ColorHelper.Argb.getArgb(120, 30, 30, 30);
    public HyperionClientV3Screen() {
        super(Text.of("Hyperion Client 3"));
        registerListeners();
    }
    private void registerListeners() {

    }

    public void setScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == this) return;
        searchString = "";
        updateModules();
        mouseClick = null;
        currentBarSize = -1;
        client.setScreen(this);
        SoundHandler.playSound(SoundEvents.BLOCK_SHULKER_BOX_OPEN);
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
        int y = renderCursorSystem(context, client);
        renderModules(context, origin, border , client, y);
    }
    private void renderStaticElements(DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        int backgroundA = ColorHelper.Argb.getArgb(200, 85, 128, 128);
        /**
        context.fill((int) origin.getX(), (int) origin.getY(), (int) border.getX(), (int) border.getY(), backgroundColor);
        int overlap = 4;
        context.fill((int) origin.getX(), (int) origin.getY(), (int) border.getX(), (int) origin.getY() + overlap, backgroundA);
        context.fill((int) border.getX() - overlap, (int) border.getY() - overlap, (int) border.getX(), (int) origin.getY() + overlap, backgroundA);
        context.fill((int) origin.getX(), (int) border.getY() - overlap, (int) origin.getX() + overlap, (int) origin.getY() + overlap, backgroundA);
        context.fill((int) origin.getX(), (int) border.getY() - overlap, (int) border.getX(), (int) border.getY(), backgroundA);
         */
        RenderUtil.fillWithBorder(context, (int) origin.getX(), (int) origin.getY(), (int) border.getX(), (int) border.getY(), backgroundColor, backgroundA, 4);

        float titleScale = 4f;
        String text = "Hyperion Client V3";
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
    private int renderCursorSystem(DrawContext context, MinecraftClient client) {
        //bar under cursor
        int y = (int) (this.height * 0.18f);
        int xCenter = this.width / 2;
        int baseSize = 55;
        int overpass = 10;
        float textScale = 2f;
        if (currentBarSize == -1) currentBarSize = baseSize;
        int maxSize = (int) Math.max(baseSize, (client.textRenderer.getWidth(searchString) + overpass) * textScale / 2);
        if (currentBarSize < maxSize) currentBarSize ++;
        if (currentBarSize > maxSize) currentBarSize --;
        int width = 2;
        context.fill(xCenter - currentBarSize, y - width, xCenter + currentBarSize, y + width, ColorHelper.Argb.getArgb(255, 100, 255, 255));
        // Calculate the X and Y positions before scaling
        int textWidth = client.textRenderer.getWidth(searchString);
        int textX = (int) ((this.width / 2) / textScale - textWidth / 2);
        int textY = (int) ((y + width) / textScale) - client.textRenderer.fontHeight;
        // Scale the matrix
        context.getMatrices().scale(textScale, textScale, textScale);
        context.drawText(client.textRenderer, searchString, textX, textY, orangeColor, false);
        context.getMatrices().scale(1 / textScale, 1 / textScale, 1 / textScale);

        return y;
    }
    private void renderModules(DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client, int cursorYHeight) {
        int maxYHeight = cursorYHeight + 30;
        int width = 70;
        int height = 15;
        int distanceBetween = 18;
        int activeColor = ColorHelper.Argb.getArgb(150, 85, 255, 115);
        int inactiveColor = ColorHelper.Argb.getArgb(150, 250, 85, 85);
        int modulesPerRow = (int) (border.getY() - maxYHeight) / height + distanceBetween;
        for (int i = 0; i < toRenderModules.size(); i++) {
            Module m = toRenderModules.get(i);
            int xCount = MathHelper.floor((float) i / (float) modulesPerRow);
            int multiplier = -1;
            if (xCount % 2 == 0) multiplier = 1;
            renderModule(m, this.width / 2 + ((xCount * multiplier) * (width + distanceBetween)), maxYHeight + (height + distanceBetween) * (i % modulesPerRow), width, height, context, activeColor, inactiveColor, client);
        }
    }
    private void renderModule(Module m, int x, int y, int width, int height, DrawContext context, int activeColor, int inactiveColor, MinecraftClient client) {
        context.fill(x - width / 2, y - height / 2, x + width / 2, y + height / 2, m.isEnabled() ? activeColor : inactiveColor);
        if (mouseClick != null && (mouseClick.x > x - (double) width / 2 && mouseClick.x < x + (double) width / 2 && mouseClick.y > y - (double) height / 2 && mouseClick.y < y + (double) height / 2)) {
            mouseClick = null;
            if (mouseButton == 0) {
                m.toggle();
            } else {
                client.setScreen(new ModuleEditScreen(m, this));
                SoundHandler.playSound(SoundEvents.ENTITY_ARROW_HIT, 2f);
            }
        }
        context.drawText(client.textRenderer, m.getName(), x - client.textRenderer.getWidth(m.getName()) / 2, (int) (y - height + client.textRenderer.fontHeight * 0.1f), Colors.BLACK, false);
        context.drawText(client.textRenderer, m.getHackType().getName(), x - client.textRenderer.getWidth(m.getHackType().getName()) / 2, (int) (y - height + 12), ColorHelper.Argb.getArgb(150, 0, 0, 0), false);
    }
    public void updateModules() {
        toRenderModules.clear();
        if (searchString.isEmpty())
            toRenderModules.addAll(ModuleHandler.getModules());
        else {
            main : for (Module m : ModuleHandler.getModules()) {
                if (m.getName().toLowerCase().contains(searchString.toLowerCase())) {
                    toRenderModules.add(m);
                    continue;
                }
                if (m.getHackType().getName().toLowerCase().startsWith(searchString.toLowerCase())) {
                    toRenderModules.add(m);
                    continue;
                }
                for (String str : m.getAlias()) {
                    if (str.toLowerCase().contains(searchString.toLowerCase())) {
                        toRenderModules.add(m);
                        continue main;
                    }
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        mouseClick = new Vector2d(mouseX, mouseY);
        mouseButton = button;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        //super.keyPressed(keyCode, scanCode, modifiers);
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            MinecraftClient.getInstance().setScreen(null);
            SoundHandler.playSound(SoundEvents.BLOCK_SHULKER_BOX_CLOSE);
            return true;
        }
        //ctr held?
        if (keyCode == InputUtil.GLFW_KEY_BACKSPACE && modifiers == 2) {
            searchString = "";
            updateModules();
        }
        if (keyCode == InputUtil.GLFW_KEY_BACKSPACE) {
            if (!searchString.isEmpty()) {
                searchString = searchString.substring(0, searchString.length() - 1);
                updateModules();
            }
            return true;
        }
        return false;
    }
    @Override
    public boolean charTyped(char chr, int modifiers) {
        InvWalk invWalk = (InvWalk) ModuleHandler.getModuleByClass(InvWalk.class);
        if (invWalk != null && invWalk.shouldBlockHyperionHacksScreenTyping()) return true;

        if (!SharedConstants.isValidChar(chr)) return false;
        searchString += Character.toString(chr);
        updateModules();
        return true;
    }


    @Override
    public boolean shouldPause() {
        return false;
    }
}
