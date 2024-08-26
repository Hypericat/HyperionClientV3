package me.hypericats.hyperionclientv3.modules;

import io.netty.buffer.Unpooled;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import net.fabricmc.fabric.mixin.networking.accessor.CustomPayloadC2SPacketAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

public class VanillaSpoofer extends Module implements SendPacketListener {
    public VanillaSpoofer() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        SendPacketData packetData = (SendPacketData) data;
        if (!(packetData.getPacket() instanceof CustomPayloadC2SPacketAccessor packet)) return;
        if(packet.getChannel().getNamespace().equals("minecraft") && packet.getChannel().getPath().equals("register")) {
            packetData.cancel();
            return;
        }
        if(packet.getChannel().getNamespace().equals("minecraft") && packet.getChannel().getPath().equals("brand") && !packet.getData().readString().contains("vanilla")) {
            packetData.setPacket(new CustomPayloadC2SPacket(CustomPayloadC2SPacket.BRAND, new PacketByteBuf(Unpooled.buffer()).writeString("vanilla")));
            return;
        }
        if(packet.getChannel().getNamespace().equals("fabric"))
            packetData.cancel();
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
