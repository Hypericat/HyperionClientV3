package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.List;

public class WaypointHandler extends Module {

    private static List<Waypoint> waypointLibrary;
    private static List<Waypoint> activeWaypoints;
    private static NumberOption<Double> maxRange;
    private static NumberOption<Double> minRange;

    public WaypointHandler() {
        super(true);
    }

    public static void addToLibrary(Waypoint waypoint) {
        waypointLibrary.add(waypoint);
    }

    public static void removeFromLibrary(Waypoint waypoint) {
        waypointLibrary.remove(waypoint);
    }
    public static boolean isActive(Waypoint waypoint) {
        return activeWaypoints.contains(waypoint);
    }
    public static void updateActive() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.world == null) return;

        activeWaypoints.clear();

        double maxRanged = maxRange == null ? Double.MAX_VALUE : maxRange.getValue() < 0d ? Double.MAX_VALUE : maxRange.getValue();
        double minRanged = minRange == null ? Double.MIN_VALUE : minRange.getValue() < 0d ? Double.MIN_VALUE : minRange.getValue();
        RegistryKey<World> dimensionType = client.world.getRegistryKey();

        for (Waypoint waypoint : waypointLibrary) {
            
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    protected void initOptions() {
        maxRange = new NumberOption<>(true, "Max Render Range", -1d);
        minRange = new NumberOption<>(true, "Min Render Range", -1d);
    }
    @Override
    public void onDisable() {

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
}
