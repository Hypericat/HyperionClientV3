package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;

public class SendPacketData extends EventData {
    public Packet<?> getPacket() {
        return getArg(0);
    }
    public PacketCallbacks getPacketCallbacks() {
        return getArg(1);
    }
    public SendPacketData(Packet<?> packet, PacketCallbacks callbacks) {
        super(packet, callbacks);
    }
}
