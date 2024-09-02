package me.hypericats.hyperionclientv3;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Waypoint {
    private String name;
    private Vec3d pos;
    private RegistryKey<World> worldDimension;
    private String identifier;
    private boolean activeAllWorlds;
    private boolean activeAllDimensions;

    public Waypoint(String name, Vec3d pos, RegistryKey<World> worldDimension, String identifier) {
        this.name = name;
        this.pos = pos;
        this.worldDimension = worldDimension;
        this.identifier = identifier;
    }

    public Waypoint(String name, Vec3d pos, MinecraftClient client) {
        this(name, pos, getCurrentDimension(client), getCurrentWorldIdentifier(client));
    }

    public Waypoint(String name, Vec3d pos, String identifier, MinecraftClient client) {
        this(name, pos, getCurrentDimension(client), identifier);
    }

    public Waypoint(String name, Vec3d pos, RegistryKey<World> worldDimension, MinecraftClient client) {
        this(name, pos, worldDimension, getCurrentWorldIdentifier(client));
    }

    public static RegistryKey<World> getCurrentDimension(MinecraftClient client) {
        return client.world == null ? World.OVERWORLD : client.world.getRegistryKey();
    }

    public static String getCurrentWorldIdentifier(MinecraftClient client) {
        if (client.isInSingleplayer()) {

        }
        return "";
    }
}
