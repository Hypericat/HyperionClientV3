package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.NoFallSafety;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.math.MathHelper;

public class NoFall extends Module implements TickListener {
    private EnumStringOption<NoFallSafety> noFallSafety;
    public NoFall() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (!canCauseFallDamage(client)) return;
        PacketUtil.sendPos(client.player.getPos().add(0, 0.2, 0));
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        noFallSafety = new EnumStringOption<>(true, "No Fall Safety", NoFallSafety.SAFE);

        options.addOption(noFallSafety);
    }
    public boolean canCauseFallDamage(MinecraftClient client) {
        //1f for most blocks
        //2f for stalagmites
        return (computeFallDamage(client.player.fallDistance, this.noFallSafety.getValue() == NoFallSafety.SAFE ? 1f : 2f, client) > 0);
    }

    private int computeFallDamage(float fallDistance, float damageMultiplier, MinecraftClient client) {
        StatusEffectInstance statusEffectInstance = client.player.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f = statusEffectInstance == null ? 0.0f : (float)(statusEffectInstance.getAmplifier() + 1);
        return MathHelper.ceil((fallDistance - 3.0f - f) * damageMultiplier);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "NoFall";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Fall Damage", "Slow Fall"};
    }
}
