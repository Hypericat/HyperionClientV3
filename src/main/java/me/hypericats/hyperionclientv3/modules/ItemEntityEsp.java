package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.RenderData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.HostileEntity;

public class ItemEntityEsp extends Esp implements RenderListener, TickListener {
    public ItemEntityEsp() {
        super(true, false);
    }
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.world == null) return;

        if (!(data instanceof RenderData renderData)) {
            initTargets(client);
            return;
        }
        super.render(getTargetStack(), renderData.getMatrices(), renderData.getTickDelta(), client);
    }
    @Override
    protected void initTargets(MinecraftClient client) {
        clearTargetStack();
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof ItemEntity itemEntity)) continue;
            if (itemEntity.distanceTo(client.player) <= super.getRange()) addToTargetStack(itemEntity);
        }
    }
    @Override
    public void onEnable() {
        EventHandler.register(RenderListener.class, this);
        EventHandler.register(TickListener.class, this);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RenderListener.class, this);
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "ItemEsp";
    }

    @Override
    public HackType getHackType() {
        return HackType.ESP;
    }

    public String[] getAlias() {
        return new String[] {"See through walls", "through walls", "boxes", "tracers", "lines", "item"};
    }
}
