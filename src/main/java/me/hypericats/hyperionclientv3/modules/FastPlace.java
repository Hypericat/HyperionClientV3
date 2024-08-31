package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.mixinInterface.IMinecraftClient;
import net.minecraft.client.MinecraftClient;

public class FastPlace extends Module implements TickListener {
    public FastPlace() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        IMinecraftClient client = (IMinecraftClient) MinecraftClient.getInstance();
        if (client.getItemUseCooldown() > 0) client.setItemUseCooldown(0);

    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "FastPlace";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Auto Clicker", "Faster Place"};
    }
}
