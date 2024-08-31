package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.EntityTargetPriority;
import me.hypericats.hyperionclientv3.enums.EntityTargetType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class AutoHit extends Module implements TickListener {
    public AutoHit() {
        super(true);
    }
    private BooleanOption targetPlayers;
    private BooleanOption targetHostileMobs;
    private BooleanOption targetPassiveMobs;
    private BooleanOption waitCooldown;

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (waitCooldown.getValue() && client.player.getAttackCooldownProgress(0) != 1.0) return;
        HitResult result = client.crosshairTarget;
        if (!(result instanceof EntityHitResult entityHitResult)) return;
        Entity entity = entityHitResult.getEntity();
        if (!targetPlayers.getValue() && entity instanceof PlayerEntity) return;
        if (!targetHostileMobs.getValue() && entity instanceof HostileEntity) return;
        if (!targetPassiveMobs.getValue() && entity instanceof PassiveEntity) return;
        if (entity instanceof PlayerEntity p && Friends.isFriend(p.getName().getString())) return;

        PacketUtil.attackEntity(entity);
        PacketUtil.doFakeHandSwing(Hand.MAIN_HAND);
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        targetPlayers = new BooleanOption(true, "Target Players", true);
        targetHostileMobs = new BooleanOption(true, "Target Hostile Mobs", true);
        targetPassiveMobs = new BooleanOption(true, "Target Passive Mobs", true);
        waitCooldown = new BooleanOption(true, "Wait Attack Cooldown", true);

        options.addOption(targetPlayers);
        options.addOption(targetHostileMobs);
        options.addOption(targetPassiveMobs);
        options.addOption(waitCooldown);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "AutoHit";
    }

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"Aura", "Swing"};
    }
}
