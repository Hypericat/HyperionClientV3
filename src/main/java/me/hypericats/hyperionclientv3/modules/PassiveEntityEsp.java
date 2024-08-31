package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderEntityListener;
import me.hypericats.hyperionclientv3.events.RenderListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.EntityRenderData;
import me.hypericats.hyperionclientv3.events.eventData.RenderData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;

public class PassiveEntityEsp extends Esp implements RenderListener, TickListener, RenderEntityListener {
    public PassiveEntityEsp() {
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
            if (isValidEntity(entity, client)) addToTargetStack(entity);
        }
    }
    public boolean isValidEntity(Entity entity, MinecraftClient client) {
        if (!(entity instanceof PassiveEntity passiveEntity)) return false;
        if (passiveEntity.distanceTo(client.player) > super.getRange()) return false;
        return true;
    }
    @Override
    public void onEnable() {
        EventHandler.register(RenderListener.class, this);
        EventHandler.register(TickListener.class, this);
        EventHandler.register(RenderEntityListener.class, this);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RenderListener.class, this);
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(RenderEntityListener.class, this);
    }

    @Override
    public String getName() {
        return "PassiveEntityEsp";
    }

    @Override
    public HackType getHackType() {
        return HackType.ESP;
    }

    public String[] getAlias() {
        return new String[] {"See through walls", "through walls", "boxes", "tracers", "lines"};
    }
}
