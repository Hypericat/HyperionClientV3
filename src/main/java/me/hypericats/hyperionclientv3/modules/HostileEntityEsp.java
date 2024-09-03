package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderEntityListener;
import me.hypericats.hyperionclientv3.events.RenderHandListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.EntityRenderData;
import me.hypericats.hyperionclientv3.events.eventData.RenderHandData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;

public class HostileEntityEsp extends Esp implements RenderHandListener, TickListener, RenderEntityListener {
    public HostileEntityEsp() {
        super(true);
    }
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.world == null) return;
        if (data instanceof EntityRenderData renderData) {
            Entity entity = renderData.getEntity();
            if (!isValidEntity(entity, client)) return;
            renderNameLabel(entity, renderData.getMatrices(), renderData.getVertexConsumer(), client);
            return;
        }
        if (!(data instanceof RenderHandData renderHandData)) {
            initTargets(client);
            return;
        }
        super.render(getTargetStack(), renderHandData.getMatrices(), renderHandData.getTickDelta(), client);
    }
    @Override
    protected void initTargets(MinecraftClient client) {
        clearTargetStack();
        for (Entity entity : client.world.getEntities()) {
            if (isValidEntity(entity, client)) addToTargetStack(entity);
        }
    }
    public boolean isValidEntity(Entity entity, MinecraftClient client) {
        if (!(entity instanceof HostileEntity hostileEntity)) return false;
        if (hostileEntity.distanceTo(client.player) > super.getRange()) return false;
        return true;
    }
    @Override
    public void onEnable() {
        EventHandler.register(RenderHandListener.class, this);
        EventHandler.register(TickListener.class, this);
        EventHandler.register(RenderEntityListener.class, this);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RenderHandListener.class, this);
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(RenderEntityListener.class, this);
    }

    @Override
    public String getName() {
        return "HostileEntityEsp";
    }

    @Override
    public HackType getHackType() {
        return HackType.ESP;
    }

    public String[] getAlias() {
        return new String[] {"See through walls", "through walls", "boxes", "tracers", "lines"};
    }
}
