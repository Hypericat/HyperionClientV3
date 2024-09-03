package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.RenderHandListener;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import me.hypericats.hyperionclientv3.events.eventData.RenderHandData;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class WaypointHandler extends Module implements PreRenderEntityListener {

    private static List<Waypoint> waypointLibrary = new ArrayList<>();

    public WaypointHandler() {
        super(true);
    }
    public void render(List<Waypoint> waypoints, MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, Camera camera, MinecraftClient client) {
        matrices.push();

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


            Vec3d waypointPos = waypoint.getPos().subtract(cameraPos);
            matrices.translate(waypointPos.x, waypointPos.y, waypointPos.z);

            matrices.multiply(client.getEntityRenderDispatcher().getRotation());

            float adjustedScale = waypoint.getScale();
            if (waypoint.isScaleScale()) {
                float distance = (float) waypointPos.distanceTo(cameraPos);
                adjustedScale *= distance * 0.1f;
            }
            matrices.scale(-adjustedScale, -adjustedScale, adjustedScale);

            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            TextRenderer textRenderer = client.textRenderer;
            String text = waypoint.getName();
            float h = -textRenderer.getWidth(text) / 2f;
            textRenderer.draw(text, h, 0f, waypoint.getColor(), false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.SEE_THROUGH, waypoint.isRenderBackground() ? 1073741824 : 0, light);

            matrices.pop();
        }


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
        EventHandler.register(PreRenderEntityListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(PreRenderEntityListener.class, this);
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
        PreRenderEntityData preRenderEntityData = (PreRenderEntityData) data;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        this.render(waypointLibrary, preRenderEntityData.getMatrices(), preRenderEntityData.getVertexConsumer(), preRenderEntityData.getTickDelta(), preRenderEntityData.getCamera(), client);
    }
}
