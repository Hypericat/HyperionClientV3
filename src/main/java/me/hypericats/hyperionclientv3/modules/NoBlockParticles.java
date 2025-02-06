package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import net.minecraft.client.MinecraftClient;

public class NoBlockParticles extends Module {
    public NoBlockParticles() {
        super(true);
    }

    // Look at BlockMixin & AbstractBlockStateMixin
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
        return "NoBlockParticles";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Particles", "Clean"};
    }
}
