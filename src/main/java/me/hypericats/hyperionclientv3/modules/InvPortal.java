package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import net.minecraft.client.MinecraftClient;

public class InvPortal extends Module {
    public InvPortal() {
        super(true);
    }

    @Override
    public void onEnable() {

    }
    //ClientPlayerEntityMixin tickNausea
    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {

    }

    @Override
    public String getName() {
        return "PortalInventory";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Portal GUI", "Inventory in Portal"};
    }
}
