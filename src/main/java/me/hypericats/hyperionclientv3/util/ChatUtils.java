package me.hypericats.hyperionclientv3.util;

import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.CharacterVisitor;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ChatUtils {
    public static MinecraftClient client = MinecraftClient.getInstance();
    public static void removeMessageIfContains(String message) {
       // try {
       //     ((IChatHud) client.inGameHud.getChatHud()).removeMessageIfContains(message);
       // } catch (IndexOutOfBoundsException ignore) {
       // }
    }
    public static void sendMessageStackTrace(Exception e) {
        if (MinecraftClient.getInstance().player == null) return;
        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement element : elements) {
            MinecraftClient.getInstance().player.sendMessage(Text.of(element.toString()));
        }
    }
    public static String Vec3dToIntString(Vec3d vec3d) {
        return "(" + (int) vec3d.x + ", " + (int) vec3d.y + ", " + (int) vec3d.z + ")";
    }
    public static String giveMeTheFuckingTextFromOrderedText(OrderedText text) {
        StringBuilder msg = new StringBuilder();
        CharacterVisitor visitor = (index1, style, codePoint) -> {
            msg.append((char)codePoint);
            return true;
        };
        text.accept(visitor);
        return (msg.toString());
    }
    public static void sendOfficial(String msg) {
        if (client.player == null) return;
        msg = msg.replaceAll("&&", "§");
        client.player.sendMessage(Text.of("§6[Hyperion Client V3] §f" + msg));
        removeMessageIfContains("[Hyperion Client V3]");
    }
    public static void sendMsg(String msg) {
        if (client.player == null) return;
        msg = msg.replaceAll("&&", "§");
        client.player.sendMessage(Text.of(msg));
    }
    public static String replaceColorCodes(String s) {
        return s.replaceAll("&&", "§");
    }
    public static void debugMousePos() {
        if (client.player == null) return;
        client.player.sendMessage(Text.of(String.valueOf(client.mouse.getX())));
    }

    public static void sendError(Object msg1) {
        String msg = format(msg1);
        if (client.player != null) client.player.sendMessage(Text.of("§c" + msg));
        HyperionClientV3Client.LOGGER.info(("§c" + msg));
    }
    public static void sendLog(Object msg1) {
        String msg = format(msg1);
        HyperionClientV3Client.LOGGER.info(("§c" + msg));
        if (client.player != null) client.player.sendMessage(Text.of("§c" + msg));
    }
    public static void sendDebug(Object msg1) {
        String msg = format(msg1);
        if (client.player != null) client.player.sendMessage(Text.of("§a" + msg));
        HyperionClientV3Client.LOGGER.info(("§a" + msg));
    }
    public static void displayHud(Object msg1) {
        String msg = format(msg1);
        if (client.player != null) client.inGameHud.setOverlayMessage(Text.of("§a" + msg), false);
        HyperionClientV3Client.LOGGER.info(("§a" + msg));
    }
    public static String format(Object obj) {
        return String.valueOf(obj).replaceAll("&&", "§");
    }

}
