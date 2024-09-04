package me.hypericats.hyperionclientv3.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypericats.hyperionclientv3.*;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.RenderHandListener;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import me.hypericats.hyperionclientv3.events.eventData.RenderHandData;
import me.hypericats.hyperionclientv3.gui.CustomWidget;
import me.hypericats.hyperionclientv3.gui.HyperionClientV3Screen;
import me.hypericats.hyperionclientv3.gui.ICustomWidget;
import me.hypericats.hyperionclientv3.gui.WaypointEditScreen;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class WaypointHandler extends Module implements ICustomWidget, PostRenderListener {

    private static List<Waypoint> waypointLibrary = new ArrayList<>();

    public WaypointHandler() {
        super(true);
    }
    public void render(List<Waypoint> waypoints, MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, Camera camera, MinecraftClient client) {
        matrices.push();

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();

        Vec3d cameraPos = camera.getPos();
        RegistryKey<World> currentDimension = client.world.getRegistryKey();

        int light = 0xF000F0;

        for (Waypoint waypoint : waypoints) {
            if (!(waypoint.getWorldDimension().getValue().equals(currentDimension.getValue()))) continue;
            if (!waypoint.isVisible()) continue;
            double dist = waypoint.getDistanceTo(client.player);
            if (waypoint.getMaxRange() != -1 && dist > waypoint.getMaxRange()) continue;
            if (waypoint.getMinRange() != -1 && dist < waypoint.getMinRange()) continue;
            matrices.push();


            Vec3d waypointPos;
            int renderDistance = client.options.getViewDistance().getValue() << 4;

            if (dist > renderDistance) {
                Vec3d delta = waypoint.getPos().subtract(cameraPos).normalize();
                waypointPos = delta.multiply(renderDistance);
            } else {
                waypointPos = waypoint.getPos().subtract(cameraPos);
                renderCube(waypoint, waypointPos, matrices, client);
            }

            matrices.translate(waypointPos.x, waypointPos.y, waypointPos.z);

            matrices.multiply(client.getEntityRenderDispatcher().getRotation());

            float adjustedScale = waypoint.getScale();
            if (waypoint.isScaleScale()) {
                float distance = (float) waypointPos.distanceTo(Vec3d.ZERO);
               adjustedScale = Math.max(adjustedScale * distance * 0.1f, adjustedScale);
        }
        matrices.scale(-adjustedScale, -adjustedScale, adjustedScale);

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            TextRenderer textRenderer = client.textRenderer;
            String text = waypoint.getName();
            float h = -textRenderer.getWidth(text) / 2f;
            textRenderer.draw(text, h, 0f, waypoint.getColor(), false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, waypoint.isRenderBackground() ? 1073741824 : 0, light);

            matrices.pop();
        }

        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();


        matrices.pop();

    }
    private static void renderCube(Waypoint waypoint, Vec3d offsetPos, MatrixStack matrices, MinecraftClient client) {
        matrices.push();
        offsetPos = new Vec3d(-offsetPos.getX(), offsetPos.getY(), -offsetPos.getZ());
        Box box = new Box(1, 1, 1, -1, -1, -1);
        int color = waypoint.getColor();
        RenderSystem.setShaderColor(ColorHelper.Argb.getRed(color) / 255f, ColorHelper.Argb.getGreen(color) / 255f, ColorHelper.Argb.getBlue(color) / 255f, 0.8f);

        matrices.multiply(client.getEntityRenderDispatcher().getRotation());
        matrices.translate(offsetPos.x, offsetPos.y, offsetPos.z);
        RenderUtil.drawOutlinedBox(box, matrices);

        matrices.pop();

    }

    public static void addToLibrary(Waypoint waypoint) {
        waypointLibrary.add(waypoint);
    }
    public static int removeAllByNameFromLibrary(String name) {
        int removedCount = 0;
        for (int i = 0; i < waypointLibrary.size(); i++) {
            Waypoint waypoint = waypointLibrary.get(i);
            if (waypoint.getName().equalsIgnoreCase(name)) {
                waypointLibrary.remove(i);
                removedCount ++;
                i--;
            }
        }
        return removedCount;
    }

    public static void removeFromLibrary(Waypoint waypoint) {
        waypointLibrary.remove(waypoint);
    }
    public static boolean isInLibrary(Waypoint waypoint) {
        return waypointLibrary.contains(waypoint);
    }
    public static List<Waypoint> getWaypointLibrary() {
        return waypointLibrary;
    }
    public static void clearWaypointLibrary() {
        waypointLibrary.clear();
    }
    public static void loadWaypointLibrary(List<Waypoint> library) {
        waypointLibrary = library;
    }

    @Override
    public void onEnable() {
        EventHandler.register(PostRenderListener.class, this);
    }

    @Override
    protected void initOptions() {
        addCustomWidgets(new CustomWidget(this, "Open Waypoint Edit Screen"));
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(PostRenderListener.class, this);
    }

    @Override
    public String getName() {
        return "Waypoints";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Markers", "Beacon"};
    }

    @Override
    public void onEvent(EventData data) {
        PostRenderData postRenderData = (PostRenderData) data;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        this.render(waypointLibrary, postRenderData.getMatrices(), postRenderData.getVertexConsumer(), postRenderData.getTickDelta(), postRenderData.getCamera(), client);
    }

    @Override
    public void execute(int button) {
        if (button != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        client.setScreen(new WaypointEditScreen(HyperionClientV3Client.hyperionClientV3Screen.getModuleEditScreen(), getWaypointLibrary()));
    }

    @Override
    public void playSound() {
        SoundHandler.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
    }
}
