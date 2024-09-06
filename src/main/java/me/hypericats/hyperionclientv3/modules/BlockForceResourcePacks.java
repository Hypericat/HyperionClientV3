package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket;

public class BlockForceResourcePacks extends Module implements RecievePacketListener {
    public BlockForceResourcePacks() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        RecievePacketData recievePacketData = (RecievePacketData) data;
        Packet<?> packet = recievePacketData.getPacket();
        if (packet instanceof ResourcePackSendS2CPacket) recievePacketData.cancel();
    }
    @Override
    public void onEnable() {
        EventHandler.register(RecievePacketListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RecievePacketListener.class, this);
    }

    @Override
    public String getName() {
        return "BlockForcePacks";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Pack", "Server Pack", "Resource"};
    }
}
