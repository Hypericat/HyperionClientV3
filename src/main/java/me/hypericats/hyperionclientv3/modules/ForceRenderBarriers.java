package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import net.minecraft.client.MinecraftClient;

public class ForceRenderBarriers extends Module {
    public ForceRenderBarriers() {
        super(true);
    }

    //BarrierBlockMixin.class

    @Override
    public void onEnable() {

    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {

    }

    @Override
    public String getName() {
        return "BarrierESP";
    }

    @Override
    public HackType getHackType() {
        return HackType.ESP;
    }

    public String[] getAlias() {
        return new String[] {"Barrier", "ESP"};
    }
}
