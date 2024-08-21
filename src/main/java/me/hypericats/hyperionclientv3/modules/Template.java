package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import net.minecraft.client.MinecraftClient;

public class Template extends Module implements TickListener {
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {

    }

    @Override
    public boolean shouldSaveState() {
        return true;
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "TemplateName";
    }

    @Override
    public HackType getHackType() {
        return HackType.OTHER;
    }

    public String[] getAlias() {
        return new String[] {"OtherName1", "OtherName2"};
    }
}
