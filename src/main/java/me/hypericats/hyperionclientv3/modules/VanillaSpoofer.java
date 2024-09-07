package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.BrandCustomPayload;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;

public class VanillaSpoofer extends Module implements SendPacketListener {
    public VanillaSpoofer() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        SendPacketData packetData = (SendPacketData) data;
        if (!(packetData.getPacket() instanceof CustomPayloadC2SPacket packet)) return;
        if(packet.payload() instanceof BrandCustomPayload)
            packetData.setPacket(new CustomPayloadC2SPacket(new BrandCustomPayload("vanilla")));
    }
    @Override
    public void onEnable() {
        EventHandler.register(SendPacketListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(SendPacketListener.class, this);
    }

    @Override
    public String getName() {
        return "VanillaSpoofer";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Fabric Hider", "Vanilla"};
    }
}
