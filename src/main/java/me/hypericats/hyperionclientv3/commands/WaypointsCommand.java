package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.Waypoint;
import me.hypericats.hyperionclientv3.WaypointSaver;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;

public class WaypointsCommand extends Command {
    public String getStart() {
        return "waypoints";
    }
    public String getProperUsage() {
        return "waypoints {add/remove/list/clear} {Waypoint Name}";
    }
    public void handle(String[] formatted) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (tryThrowErrorReturn(formatted.length < 2, "Invalid argument count specified!")) return;

        String type = formatted[1].toLowerCase();
        if (type.equals("list") || type.equals("ls")) {
            list();
            return;
        }
        if (type.equals("clear") || type.equals("reset") || type.equals("removeall")) {
            clear();
            feedBack("Removed all waypoints!");
            return;
        }

        if (tryThrowErrorReturn(formatted.length < 3, "Missing or invalid argument(s)")) return;
        if (type.equals("add") || type.equals("create")) {
            add(buildName(formatted));
            WaypointSaver.saveCurrentWaypoints();
            return;
        }
        if (type.equals("remove") || type.equals("delete")) {
            remove(buildName(formatted));
            WaypointSaver.saveCurrentWaypoints();
            return;
        }

        throwError("Missing or invalid argument(s)");
    }
    private String buildName(String[] formatted) {
        StringBuilder waypointNameBuilder = new StringBuilder();
        for (int i = 2; i < formatted.length; i++) {
            waypointNameBuilder.append(formatted[i]).append(" ");
        }
        //remove extra space
        if (!waypointNameBuilder.isEmpty()) waypointNameBuilder.deleteCharAt(waypointNameBuilder.length() - 1);

        return waypointNameBuilder.toString();
    }
    private void clear() {
        WaypointHandler.clearWaypointLibrary();
        WaypointSaver.saveCurrentWaypoints();
    }
    private void list() {
        List<String> text = new ArrayList<>();
        text.add(" ");
        text.add("&&6-------------------------------------------");
        for (Waypoint waypoint : WaypointHandler.getWaypointLibrary()) {
            text.add("&&c" + waypoint.getName() + "&&f : &&a" + ChatUtils.Vec3dToIntString(waypoint.getPos()));
        }
        text.add("&&6-------------------------------------------");
        text.add(" ");
        for (String str : text) {
            ChatUtils.sendMsg(str);
        }
    }
    private void add(String name) {
        if (MinecraftClient.getInstance().player == null) throwError("Player is null!");
        Waypoint waypoint = new Waypoint(name, MinecraftClient.getInstance().player.getBoundingBox().getCenter(), MinecraftClient.getInstance());
        WaypointHandler.addToLibrary(waypoint);
        feedBack("Successfully added waypoint &&c" + name + "&&a at &&f" + ChatUtils.Vec3dToIntString(waypoint.getPos()));
    }
    private void remove(String name) {
        if (MinecraftClient.getInstance().player == null) throwError("Player is null!");
        int removed = WaypointHandler.removeAllByNameFromLibrary(name);
        feedBack("Successfully &&cremoved &&f" + removed + "&&a waypoint(s)!");
    }

}
