package me.hypericats.hyperionclientv3.mixinInterface;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.world.level.storage.LevelStorage;

public interface IMinecraftServer {
    public LevelStorage.Session getLevelStorageSession();
}
