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
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class BlockServerLookReset extends Module implements RecievePacketListener {
    public BlockServerLookReset() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        RecievePacketData packetData = (RecievePacketData) data;
        Packet<?> packet = packetData.getPacket();
        if (!(packet instanceof PlayerPositionLookS2CPacket playerPositionLookS2CPacket)) return;
       packetData.setNewPacket(new PlayerPositionLookS2CPacket(playerPositionLookS2CPacket.getX(), playerPositionLookS2CPacket.getY(), playerPositionLookS2CPacket.getZ(), client.player.getYaw(), client.player.getPitch(), playerPositionLookS2CPacket.getFlags(), playerPositionLookS2CPacket.getTeleportId()));
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
        return "BlockServerLookReset";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Look Reset", "Rubber Band"};
    }
}
