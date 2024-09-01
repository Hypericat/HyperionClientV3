package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.mixinInterface.IChatHud;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.Window;
import net.minecraft.util.math.MathHelper;

public class CopyMessage {
    public static final int modifierKey = InputUtil.GLFW_KEY_LEFT_CONTROL;
    public static void onMouseClick(double x, double y, int button) {
        if (button != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        Window window = client.getWindow();
        if (!InputUtil.isKeyPressed(window.getHandle(), modifierKey)) return;
        String str = ((IChatHud)client.inGameHud.getChatHud()).getMessageAtCords(x, y);
        if (str == null || str.isEmpty()) return;
        SelectionManager.setClipboard(client, str);
        ChatUtils.displayHud("&&aCopied &&6" + str);
    }
}
