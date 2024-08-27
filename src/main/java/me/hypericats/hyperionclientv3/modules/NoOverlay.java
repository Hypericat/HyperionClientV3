package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameOverlayRendererListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameOverlayRendererData;
import net.minecraft.client.MinecraftClient;

public class NoOverlay extends Module implements InGameOverlayRendererListener {
    public NoOverlay() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        data.cancel();
    }
    @Override
    public void onEnable() {
        EventHandler.register(InGameOverlayRendererListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(InGameOverlayRendererListener.class, this);
    }

    @Override
    public String getName() {
        return "NoOverlay";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Render Overlay", "No Clip"};
    }
}
