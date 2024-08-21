package me.hypericats.hyperionclientv3.mixinInterface;

import net.minecraft.network.packet.Packet;

public interface IClientConnection {
    public void sendPacketImmediately(Packet<?> packet);
}
