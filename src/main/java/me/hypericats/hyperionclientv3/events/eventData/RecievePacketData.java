package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

public class RecievePacketData extends EventData {
    private Packet<?> newPacket;
    public void setNewPacket(Packet<?> packet) {
        this.newPacket = packet;
    }
    public Packet<?> getNewPacket() {
        return newPacket;
    }
    public Packet<?> getPacket() {
        return getArg(0);
    }
    public PacketListener getPacketListener() {
        return getArg(1);
    }
    public RecievePacketData(Packet<?> packet, PacketListener listener) {
        super(packet, listener);
    }
}
