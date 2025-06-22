package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.mixinInterface.IMinecraftServer;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.net.InetSocketAddress;
import java.util.EnumSet;

public class Waypoint {
    private String name;
    private Vec3d pos;
    private RegistryKey<World> worldDimension;
    private String identifier;
    private boolean visible;
    private int color;
    private boolean renderBackground;
    private boolean scaleScale;
    private float scale;
    private double maxRange;
    private double minRange;
    public Waypoint(String name, Vec3d pos, RegistryKey<World> worldDimension, String identifier, int color, boolean visible, float scale, double maxRange, double minRange, boolean scaleScale, boolean renderBackground) {
        this.name = name;
        this.pos = pos.floorAlongAxes(EnumSet.of(Direction.Axis.Y));
        this.worldDimension = worldDimension;
        this.identifier = identifier;
        this.color = color;
        this.visible = visible;
        this.scale = scale;
        this.maxRange = maxRange;
        this.minRange = minRange;
        this.scaleScale = scaleScale;
        this.renderBackground = renderBackground;
    }


    public Waypoint(String name, Vec3d pos, RegistryKey<World> worldDimension, String identifier, int color, boolean visible) {
        this(name, pos, worldDimension, identifier, color, visible, 0.03f, -1d, -1d, true,false);
    }

    public Waypoint(String name, Vec3d pos, int color, boolean visible, MinecraftClient client) {
        this(name, pos, getCurrentDimension(client), getCurrentWorldIdentifier(client), color, visible);
    }

    public Waypoint(String name, Vec3d pos, MinecraftClient client) {
        this(name, pos, getCurrentDimension(client), getCurrentWorldIdentifier(client), -1, true);
    }

    public static RegistryKey<World> getCurrentDimension(MinecraftClient client) {
        return client.world == null ? World.OVERWORLD : client.world.getRegistryKey();
    }

    public static String getCurrentWorldIdentifier(MinecraftClient client) {
        if (client.isInSingleplayer()) {
            if (client.getServer() == null) {
                throw new RuntimeException("Tried to getIdentifier of current singePlayer world while not in a singePlayer world");
            }
            return ((IMinecraftServer) client.getServer()).getLevelStorageSession().getDirectoryName();
        }
        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
        if (networkHandler == null) {
            throw new RuntimeException("Tried to getIdentifier with null player");
        }
        ServerInfo info = client.getNetworkHandler().getServerInfo();
        InetSocketAddress address = (InetSocketAddress) client.getNetworkHandler().getConnection().getAddress();
        if (info == null) {
            throw new RuntimeException("Tried to getIdentifier of current multiplayer server world while not in a multiplayer server");
        }
        //remove the "." in to not fuck with the file system
        return (address.getAddress().getHostAddress() + address.getPort() + address.getHostName()).replaceAll("\\.", "");
    }
    public String toString() {
        return name + ":" + pos.toString() + ":" + worldDimension.getRegistry().toString() + ":" + identifier + ":" + visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vec3d getPos() {
        return pos;
    }

    public void setPos(Vec3d pos) {
        this.pos = pos;
    }

    public RegistryKey<World> getWorldDimension() {
        return worldDimension;
    }

    public void setWorldDimension(RegistryKey<World> worldDimension) {
        this.worldDimension = worldDimension;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getColor() {
        return color;
    }
    public int getColorFullAlpha() {
        return ColorHelper.getArgb(255, ColorHelper.getRed(this.getColor()), ColorHelper.getGreen(this.getColor()), ColorHelper.getBlue(this.getColor()));
    }

    public void setColor(int color) {
        this.color = color;
    }
    public double getDistanceTo(Vec3d pos) {
        return getPos().distanceTo(pos);
    }
    public double getDistanceTo(Entity e) {
        return getDistanceTo(e.getBoundingBox().getCenter());
    }

    public boolean isRenderBackground() {
        return renderBackground;
    }

    public void setRenderBackground(boolean renderBackground) {
        this.renderBackground = renderBackground;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public double getMinRange() {
        return minRange;
    }

    public void setMinRange(double minRange) {
        this.minRange = minRange;
    }

    public boolean isScaleScale() {
        return scaleScale;
    }

    public void setScaleScale(boolean scaleScale) {
        this.scaleScale = scaleScale;
    }
    public double getX() {
        return pos.x;
    }

    public void setX(double x) {
        this.pos = new Vec3d(x, pos.y, pos.z);
    }

    public double getY() {
        return pos.y;
    }

    public void setY(double y) {
        this.pos = new Vec3d(pos.x, y, pos.z);
    }

    public double getZ() {
        return pos.z;
    }

    public void setZ(double z) {
        this.pos = new Vec3d(pos.x, pos.y, z);
    }
}
