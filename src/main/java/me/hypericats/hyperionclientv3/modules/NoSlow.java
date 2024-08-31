package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import net.minecraft.client.MinecraftClient;

public class NoSlow extends Module {
    //ClientPlayerEntityMixin.class for eating
    //BlockMixin.class for blocks
    public NoSlow() {
        super(true);
    }

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
        return "NoSlow";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"No slowdown", "slime", "honey", "eat"};
    }
}
