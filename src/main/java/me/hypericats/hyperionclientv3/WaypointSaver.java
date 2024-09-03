package me.hypericats.hyperionclientv3;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.DisconnectListener;
import me.hypericats.hyperionclientv3.events.PostJoinWorldListener;
import me.hypericats.hyperionclientv3.events.PreJoinWorldListener;
import me.hypericats.hyperionclientv3.events.eventData.PostJoinWorldData;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import me.hypericats.hyperionclientv3.util.FileUtil;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WaypointSaver implements PostJoinWorldListener, DisconnectListener, PreJoinWorldListener {
    public static File waypointsDir = new File(FileUtil.HypCv3Dir.getAbsolutePath() + "\\Waypoints");
    public void init() {
        EventHandler.register(PreJoinWorldListener.class, this);
        EventHandler.register(PostJoinWorldListener.class, this);
        EventHandler.register(DisconnectListener.class, this);
        initDir();
    }

    public static void saveCurrentWaypoints() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        if (client.player == null) return;

        for (int i = 0; i < 5; i++) {
            System.out.println("Saving Waypoints");
        }

        List<Waypoint> waypoints = WaypointHandler.getWaypointLibrary();

        String worldIdentifier = Waypoint.getCurrentWorldIdentifier(client);
        File file = getFileFromIdentifier(worldIdentifier);

        //return and delete saved waypoints if no existing waypoints
        if (waypoints.isEmpty() && file.exists()) {
            file.delete();
            return;
        }

        FileUtil.createFile(file);
        Gson gson = new Gson();
        String fileContents = gson.toJson(waypoints);

        FileUtil.overwriteToFile(fileContents, file);
    }
    public static void loadLibraryFromFile() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        if (client.player == null) return;

        for (int i = 0; i < 5; i++) {
            System.out.println("Loading Waypoints!");
        }

        String worldIdentifier = Waypoint.getCurrentWorldIdentifier(client);
        File file = getFileFromIdentifier(worldIdentifier);

        //if no saved waypoints load empty list
        if (!file.exists()) {
            WaypointHandler.loadWaypointLibrary(new ArrayList<>());
            return;
        }

        Gson gson = new Gson();
        List<String> json = FileUtil.readFromFile(file);

        if (json == null || json.isEmpty()) {
            file.delete();
            WaypointHandler.loadWaypointLibrary(new ArrayList<>());
            return;
        }
        List<Waypoint> library;
        Type listType = new TypeToken<List<Waypoint>>() {}.getType();

        try {
            library = gson.fromJson(json.get(0), listType);
        } catch (JsonSyntaxException e) {
            library = null;
            e.printStackTrace();
        }

        if (library == null) {
            file.delete();
            WaypointHandler.loadWaypointLibrary(new ArrayList<>());
            return;
        }

        WaypointHandler.loadWaypointLibrary(library);
    }
    public static File getFileFromIdentifier(String currentIdentifier) {
        return new File(waypointsDir + "\\" + currentIdentifier + ".wyp");
    }
    public static void initDir() {
        FileUtil.createDir(waypointsDir);
    }
    @Override
    public void onEvent(EventData data) {
        if (data instanceof PostJoinWorldData) {
            loadLibraryFromFile();
            return;
        }
        saveCurrentWaypoints();
    }
}
