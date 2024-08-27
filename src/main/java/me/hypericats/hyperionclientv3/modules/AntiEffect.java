package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;

public class AntiEffect extends Module implements RecievePacketListener {
    public AntiEffect() {
        super(true);
    }
    private BooleanOption blockBlindess;
    private BooleanOption blockDarkness;
    private BooleanOption blockNausea;
    private BooleanOption blockWither;
    @Override
    public void onEvent(EventData data) {
        RecievePacketData packetData = (RecievePacketData) data;
        if (!(packetData.getPacket() instanceof EntityStatusEffectS2CPacket effectPacket)) return;
        if (shouldBlockEffect(effectPacket.getEffectId())) data.cancel();
    }
    private boolean shouldBlockEffect(StatusEffect effect) {
        if (blockBlindess.getValue() && effect == StatusEffects.BLINDNESS) return true;
        if (blockNausea.getValue() && effect == StatusEffects.NAUSEA) return true;
        if (blockDarkness.getValue() && effect == StatusEffects.DARKNESS) return true;
        if (blockWither.getValue() && effect == StatusEffects.WITHER) return true;

        return false;
    }
    @Override
    public void onEnable() {
        EventHandler.register(RecievePacketListener.class, this);
    }

    @Override
    protected void initOptions() {
        blockBlindess = new BooleanOption(true, "Block Blindess", true);
        blockNausea = new BooleanOption(true, "Block Nausea", true);
        blockDarkness = new BooleanOption(true, "Block Darkness", true);
        blockWither = new BooleanOption(true, "Block Wither", true);

        options.addOption(blockBlindess);
        options.addOption(blockNausea);
        options.addOption(blockDarkness);
        options.addOption(blockWither);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RecievePacketListener.class, this);
    }

    @Override
    public String getName() {
        return "AntiEffect";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Blindness", "Darkness", "Anti Darkness", "Anti Blindness", "AntiDarkness", "Dark", "Anti Nausea", "AntiNausea", "Anti Wither", "AntiWither", "Block Effect"};
    }
}
