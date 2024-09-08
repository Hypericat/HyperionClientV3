package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.ViewInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class RepCommand extends Command implements TickListener {
    public RepCommand() {
        EventHandler.register(TickListener.class, this);
    }
    long tickCounter = 0;

    public String getStart() {
        return "reps";
    }
    public String getProperUsage() {
        return "reps {INT Tick Interval || STRING clear} {STRING Message}";
    }
    private final List<Pair<Integer, String>> reps = new ArrayList<>();

    public void handle(String[] formatted) {
        if (tryThrowErrorReturn(formatted.length < 2, "Not enough arguments provided")) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (formatted[1].equals("clear") || formatted[1].equals("remove")) {
            reps.clear();
            feedBack("Removed all reps!");
            return;
        }
        if (tryThrowErrorReturn(formatted.length < 3, "Not enough arguments provided")) return;
        int tickInterval;
        try {
            tickInterval = Integer.parseInt(formatted[1]);
        } catch (Exception e) {
            throwError("Error non integer value provided as interval!");
            return;
        }
        if (tryThrowErrorReturn(tickInterval < 1, "Invalid Tick Interval provided!")) return;
        StringBuilder chatMessage = new StringBuilder();
        for (int i = 2; i < formatted.length; i++) {
            chatMessage.append(formatted[i]).append(" ");
        }
        if (!chatMessage.isEmpty()) chatMessage.deleteCharAt(chatMessage.length() - 1);
        String message = chatMessage.toString();
        reps.add(new Pair<>(tickInterval, message));
        feedBack("Repeating&&c" + message + "&&a every &&c" + tickInterval + "&&a ticks!");
    }

    @Override
    public void onEvent(EventData data) {
        tickCounter ++;
        for (Pair<Integer, String> p : reps) {
            int interval = p.getLeft();
            if (tickCounter % interval == 0) {
                sendMessage(p.getRight());
            }
        }
    }



    public void sendMessage(String chatText) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.inGameHud == null) return;
        if ((chatText = this.normalize(chatText)).isEmpty()) {
            return;
        }
        if (chatText.startsWith("/")) {
            client.player.networkHandler.sendChatCommand(chatText.substring(1));
        } else {
            client.player.networkHandler.sendChatMessage(chatText);
        }
    }
    public String normalize(String chatText) {
        return StringHelper.truncateChat(StringUtils.normalizeSpace(chatText.trim()));
    }
}
