package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.events.ModuleToggleListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameHudRenderData;
import me.hypericats.hyperionclientv3.events.eventData.ModuleToggleData;
import me.hypericats.hyperionclientv3.util.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HackGUI extends Module implements InGameHudRenderListener, ModuleToggleListener {
    List<String> moduleListOrdered = new ArrayList<>();

    public HackGUI() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {

        if (data instanceof ModuleToggleData) {
            updateModuleListOrder((ModuleToggleData) data);
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.getWindow() == null) return;
        if (MinecraftClient.getInstance().player == null) return;

        InGameHudRenderData renderData = (InGameHudRenderData) data;

        float x = (float) (MinecraftClient.getInstance().getWindow().getScaledWidth() - 2);
        float y = MinecraftClient.getInstance().getWindow().getScaledHeight() - 13;

        int textColor = ColorUtils.getRgbColor();

        Vec3d pos = MinecraftClient.getInstance().player.getPos();

        DrawContext context = ((InGameHudRenderData) data).getDrawContext();
        //if (KeyInputHandler.FreeCam && !KeyInputHandler.Blink) {
        //    MinecraftClient.getInstance().textRenderer.draw(matrixStack, "Freecam", (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) - MinecraftClient.getInstance().textRenderer.getWidth("Freecam") / 2f, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f - 10f, textColor);
        //}
        //if (KeyInputHandler.Blink && !KeyInputHandler.FreeCam) {
        //    MinecraftClient.getInstance().textRenderer.draw(matrixStack, "Blink", (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) - MinecraftClient.getInstance().textRenderer.getWidth("Blink") / 2f, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f - 10f, textColor);
        //}
        //if (KeyInputHandler.Blink && KeyInputHandler.FreeCam) {
        //    MinecraftClient.getInstance().textRenderer.draw(matrixStack, "Blink and Freecam", (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2f) - MinecraftClient.getInstance().textRenderer.getWidth("Blink and Freecam") / 2f, MinecraftClient.getInstance().getWindow().getScaledHeight() / 2f - 10f, textColor);
        //}
        if (!client.options.debugEnabled) {
            client.textRenderer.draw("FPS " + client.getCurrentFps(), 2f, 2f, textColor, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, ColorHelper.Argb.getArgb(0, 0, 0, 0), 0xF000F0);
            client.textRenderer.draw( "Location X: " + (int) pos.x + " Y: " + (int) pos.y + " Z: " + (int) pos.z, 2f, 12f, textColor, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, ColorHelper.Argb.getArgb(0, 0, 0, 0), 0xF000F0);
        }
        for (String str : moduleListOrdered) {
            client.textRenderer.draw(str, x - client.textRenderer.getWidth(str), y, textColor, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, ColorHelper.Argb.getArgb(0, 0, 0, 0), 0xF000F0);
            y -= 10;
        }
    }
    public void updateModuleListOrder(ModuleToggleData data) {
        List<String> modules = new ArrayList<>();
        for (Module module : ModuleHandler.getModules()) {
            if (module.isEnabled()) modules.add(module.getName());
        }
        sortModuleSize(modules);
        moduleListOrdered = modules;
    }
    private void sortModuleSize(List<String> moduleList) {
        //sort the list by string length and then alphabetically within each length group
        moduleList.sort(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));

        // create a new list that prioritizes shorter names first
        List<String> tempList = new ArrayList<>();
        for (int i = 1; i <= moduleList.get(moduleList.size() - 1).length(); i++) {
            for (String str : moduleList) {
                if (str.length() == i) {
                    tempList.add(str);
                }
            }
        }
        MinecraftClient client = MinecraftClient.getInstance();
        tempList.sort((str1, str2) -> {
            int length1 = client.textRenderer.getWidth(str1);
            int length2 = client.textRenderer.getWidth(str2);
            int result = Integer.compare(length2, length1);
            if (result == 0) {
                result = str1.compareTo(str2);
            }
            return result;
        });

        // replace the original list with the new one
        moduleList.clear();
        moduleList.addAll(tempList);
        tempList.clear();
    }
    @Override
    public void onEnable() {
        EventHandler.register(InGameHudRenderListener.class, this);
        EventHandler.register(ModuleToggleListener.class, this);
    }

    @Override
    protected void initOptions() {

    }

    @Override
    public void onDisable() {
        EventHandler.unregister(InGameHudRenderListener.class, this);
        EventHandler.unregister(ModuleToggleListener.class, this);
    }

    @Override
    public String getName() {
        return "HackGUI";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Hack List", "Hack List Display", "Coordinates", "Fps"};
    }
}
