package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.GetHitboxListener;
import me.hypericats.hyperionclientv3.events.eventData.GetHitboxData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

public class BiggerHitboxes extends Module implements GetHitboxListener {
    private NumberOption<Double> boundingBoxAddedSize;
    private BooleanOption targetPlayers;
    private BooleanOption targetHostileMobs;
    private BooleanOption targetPassiveMobs;
    private BooleanOption targetLiving;
    private BooleanOption targetOther;
    private boolean singlePlayerWarn = false;

    public BiggerHitboxes() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.isInSingleplayer()) {
            if (!singlePlayerWarn) {
                ChatUtils.sendError(this.getName() + " does not work in singlePlayer, it will not work while in this world!");
                singlePlayerWarn = true;
            }
            return;
        }

        GetHitboxData hitboxData = (GetHitboxData) data;
        Entity entity = hitboxData.getEntity();
        if (!isValidEntity(entity, client)) return;
        Box oldHitbox = hitboxData.getOriginalBoundingBox();
        hitboxData.setNewBox(oldHitbox.expand(boundingBoxAddedSize.getValue()));
    }
    private boolean isValidEntity(Entity entity, MinecraftClient client) {
        if (targetPlayers.getValue() && entity instanceof PlayerEntity && !(entity instanceof FakePlayerEntity) && entity.getId() != client.player.getId()) return true;
        if (targetHostileMobs.getValue() && entity instanceof HostileEntity) return true;
        if (targetPassiveMobs.getValue() && entity instanceof PassiveEntity) return true;
        if (targetLiving.getValue() && entity instanceof LivingEntity && !(entity instanceof FakePlayerEntity) && entity.getId() != client.player.getId()) return true;
        if (targetOther.getValue() && !(entity instanceof LivingEntity)) return true;
        return false;
    }
    @Override
    public void onEnable() {
        EventHandler.register(GetHitboxListener.class, this);
        singlePlayerWarn = false;
    }

    @Override
    protected void initOptions() {
        boundingBoxAddedSize = new NumberOption<>(true, "Extra Size", 0.2d);
        targetPlayers = new BooleanOption(true, "Target Players", true);
        targetHostileMobs = new BooleanOption(true, "Target Hostile", true);
        targetPassiveMobs = new BooleanOption(true, "Target Passive", true);
        targetLiving = new BooleanOption(true, "Target All Living", true);
        targetOther = new BooleanOption(true, "Target Other", true);

        options.addOption(boundingBoxAddedSize);
        options.addOption(targetPlayers);
        options.addOption(targetHostileMobs);
        options.addOption(targetPassiveMobs);
        options.addOption(targetLiving);
        options.addOption(targetOther);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(GetHitboxListener.class, this);
    }

    @Override
    public String getName() {
        return "BoundingBox";
    }

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"Hitbox", "Bigger Bounding Box", "Aim"};
    }
}
