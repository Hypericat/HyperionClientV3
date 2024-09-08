package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.ViewInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class InvCommand extends Command implements InGameHudRenderListener {
    public String getStart() {
        return "inv";
    }
    public String getProperUsage() {
        return "inv {STRING Player Name}";
    }
    private String targetName;
    public void handle(String[] formatted) {
        if (formatted.length < 2) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        targetName = formatted[1];
        EventHandler.register(InGameHudRenderListener.class, this);
    }
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || targetName == null || targetName.isEmpty()) {
            close();
            return;
        }
        OtherClientPlayerEntity target = null;
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof OtherClientPlayerEntity player)) continue;
            if (player.getName().getString().equalsIgnoreCase(targetName)) target = player;
        }
        if (tryThrowErrorReturn(target == null, "Could not find specified player!")) {
            close();
            return;
        }
        client.setScreen(new ViewInventoryScreen(target));
        close();
    }
    private void close() {
        EventHandler.unregisterNext(InGameHudRenderListener.class, this);
    }
}
