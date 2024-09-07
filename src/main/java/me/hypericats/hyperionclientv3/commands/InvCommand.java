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
    public void handle(String[] formatted, boolean b) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (tryThrowErrorReturn(client.player.isCreative(), "This command cannot be used while in creative mode!")) return;
        if (tryThrowErrorReturn(formatted.length < 2, "Invalid argument count specified!")) return;
        String strCount = formatted[1];
        int amount;
        try {
            amount = Integer.parseInt(strCount);
        } catch (Exception e) {
            throwError("Loop amount was not an integer!");
            return;
        }
        client.player.sendMessage(Text.of("test"));
        if (tryThrowErrorReturn(!client.player.isOnGround(), "You must be on the ground to use this!")) return;
        Vec3d pos = client.player.getPos();

        for(int i = 0; i < 80; i++)
        {
            PacketUtil.sendPos(pos.x, pos.y + amount + 2.1, pos.z, false);
            PacketUtil.sendPos(pos.x, pos.y + 0.05, pos.z, false);
        }

        PacketUtil.sendPos(pos.x, pos.y, pos.z, true);
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
