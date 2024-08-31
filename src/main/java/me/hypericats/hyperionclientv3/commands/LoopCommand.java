package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.StringHelper;
import org.apache.commons.lang3.StringUtils;

public class LoopCommand extends Command {
    public String getStart() {
        return "loop";
    }
    public String getProperUsage() {
        return "loop {INT COUNT} {COMMAND/MESSAGE}";
    }
    public void handle(String[] formatted) {
        if (tryThrowErrorReturn(formatted.length < 3, "Invalid argument count specified!")) return;
        String strCount = formatted[1];
        int count;
        try {
            count = Integer.parseInt(strCount);
        } catch (Exception e) {
            throwError("Loop amount was not an integer!");
            return;
        }
        StringBuilder chatMessage = new StringBuilder();
        for (int i = 2; i < formatted.length; i++) {
            chatMessage.append(formatted[i]).append(" ");
        }
        String message = chatMessage.toString();

        for (int i = 0; i < count; i++) {
            this.sendMessage(message);
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
