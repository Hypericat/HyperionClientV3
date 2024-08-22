package me.hypericats.hyperionclientv3;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;

public class SoundHandler {
    public static void playSound(SoundEvent event) {
        playSound(event, 1f);
    }
    public static void playSound(SoundEvent event, float pitch) {
        playSound(event, 1f, pitch);
    }
    public static void playSound(SoundEvent event, float volume, float pitch) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) client.player.playSound(event, volume, pitch);
    }
}
