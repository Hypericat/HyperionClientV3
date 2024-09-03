package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class DamageCommand extends Command {
    public String getStart() {
        return "damage";
    }
    public String getProperUsage() {
        return "damage {INT AMOUNT}";
    }
    public void handle(String[] formatted) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (tryThrowErrorReturn(formatted.length < 2, "Invalid argument count specified!")) return;
        String strCount = formatted[1];
        int amount;
        try {
            amount = Integer.parseInt(strCount);
        } catch (Exception e) {
            throwError("Loop amount was not an integer!");
            return;
        }
        if (tryThrowErrorReturn(!client.player.isOnGround(), "You must be on the ground to use this!")) return;
        Vec3d pos = client.player.getPos();
        Vec3d airPos = client.player.getPos().add(0, amount + 2.1, 0);
        PlayerUtils.packetTpToPos(airPos, client, false, pos, false);
        PacketUtil.sendPos(pos.x, pos.y + 0.05, pos.z, false);

        PlayerUtils.packetTpToPos(pos, client, false, pos, false);
        PacketUtil.sendPos(pos, true);
        feedBack("Dealt &&c" + amount + "&&a half hearts of damage!");
    }
}
