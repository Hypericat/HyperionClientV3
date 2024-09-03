package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.util.ChatUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends Command {
    public String getStart() {
        return "help";
    }
    public String getProperUsage() {
        return "help";
    }
    public void handle(String[] formatted) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        List<String> text = new ArrayList<>();
        text.add(" ");
        text.add("&&6-------------------------------------------");
        for (Command command : CommandHandlerDispatcher.getCommands()) {
            text.add("&&c" + command.getStart() + "&&f : &&6" + command.getProperUsage());
        }
        text.add("&&6-------------------------------------------");
        text.add(" ");
        for (String str : text) {
            ChatUtils.sendMsg(str);
        }
    }
    public void handle(String[] formatted, boolean b) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.sendMessage(Text.of("test"));
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

}
