package me.hypericats.hyperionclientv3.gui;

import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.SliderOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import org.joml.Math;

public class SliderOptionsWidget extends ModuleOptionsWidget {
    public SliderOptionsWidget(SliderOption<?> option) {
        super(option);
        updateHandlePercentage();
    }
    private int width = 100;
    private int height = 20;
    private double handlePercentage;
    private boolean playSound = true;


    @Override
    public void render(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
        int thickness = 2;
        int yPos = y + this.height / 2;
        //line
        context.fill(x - this.getWidth() / 2, yPos - thickness, x + this.getWidth() / 2, yPos + thickness, ColorHelper.getArgb(255, 100, 255, 255));

        //handle
        int size = 6;
        int xPos = (int) (this.getWidth() * handlePercentage) + x - this.getWidth() / 2;
        context.fill(xPos - size, yPos - size, xPos + size, yPos + size, HyperionClientV3Screen.orangeColor);

        //widget name
        context.drawText(client.textRenderer, option.getName(), x - client.textRenderer.getWidth(option.getName()) / 2, y - client.textRenderer.fontHeight, Colors.BLACK, false);

        float textScale = 1.5f;
        //get rid of floating point errors
        String string = option.getValue().toString();
        if (string.contains(".")) {
            if (string.indexOf(".") < string.length() - 3) {
                string = string.substring(0, string.indexOf(".") + 2);
            }
        }
        // Calculate the X and Y positions before scaling
        int textWidth = client.textRenderer.getWidth(string);
        int textX = (int) (x / textScale - textWidth / 2);
        int textY = (int) ((int) ((y) / textScale) + (client.textRenderer.fontHeight / textScale) * 2);
        // Scale the matrix

        context.getMatrices().scale(textScale, textScale, textScale);
        context.drawText(client.textRenderer, string, textX, textY, HyperionClientV3Screen.orangeColor, false);
        context.getMatrices().scale(1 / textScale, 1 / textScale, 1 / textScale);
    }
    public void updateHandlePercentage() {
        SliderOption<?> option = (SliderOption<?>) this.option;
        option.setValue(Math.clamp(option.getMinValue().doubleValue(), option.getMaxValue().doubleValue(), option.getValue().doubleValue()));
        handlePercentage = (option.getValue().doubleValue() - option.getMinValue().doubleValue()) / (option.getMaxValue().doubleValue() - option.getMinValue().doubleValue());
    }
    public void setOption(int x) {
        if (x < 0) x = 0;
        x = Math.min(x, width);

        double percentage = (double) x / this.getWidth();
        SliderOption<?> option = (SliderOption<?>) this.option;
        double addValue = ((option.getMaxValue().doubleValue() -  option.getMinValue().doubleValue()) * percentage);
        double value = option.getMinValue().doubleValue() + addValue;
        double sliderSnap = option.getSliderSnap().doubleValue();
        if (sliderSnap > 0d) {
            int iterations = (int) Math.round(value / sliderSnap);
            value = iterations * sliderSnap;
        }
        option.setValue(value);
        updateHandlePercentage();
    }

    @Override
    public void updatePos(int x, int y, DrawContext context, Vector2f origin, Vector2f border, MinecraftClient client) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void click(double x, double y, int button) {
        setOption((int) (x - (this.getX() - this.getWidth() / 2)));
        if (playSound) {
            SoundHandler.playSound(SoundEvents.BLOCK_PISTON_EXTEND, 0.5f);
            playSound = false;
        }
    }
    public void onRelease() {
        playSound = true;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        return (int) (height + (renderer == null ? 15 : renderer.fontHeight * 1.5f));
    }
}
