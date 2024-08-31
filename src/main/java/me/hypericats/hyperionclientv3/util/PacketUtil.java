package me.hypericats.hyperionclientv3.util;

import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import me.hypericats.hyperionclientv3.mixinInterface.ILivingEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class PacketUtil {
    static MinecraftClient client = MinecraftClient.getInstance();
    public static void sendPos(double x, double y, double z, boolean onGround)
    {
        if (client.player == null) return;
        client.player.networkHandler.sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, onGround));
    }
    public static void sendPos(Vec3d pos) {
        sendPos(pos, client.player.isOnGround());
    }
    public static void sendPos(Vec3d pos, boolean onGround) {
        if (client.player == null) return;
        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, client.player.isOnGround());
        send(packet);
    }
    public static void sendPosImmediately(Vec3d pos, boolean onGround) {
        if (client.player == null) return;
        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, onGround);
        sendImmediately(packet);
    }
    public static void sendPosImmediately(Vec3d pos) {
        if (client.player == null) return;
        sendPosImmediately(pos, client.player.isOnGround());
    }
    public static void doFakeHandSwing(Hand hand) {
        if (client.player == null) return;

        if (!client.player.handSwinging || client.player.handSwingTicks >= ((ILivingEntity) client.player).getHandSwingingTicks() / 2 || client.player.handSwingTicks < 0) {
            client.player.handSwingTicks = -1;
            client.player.handSwinging = true;
            client.player.preferredHand = hand;
        }
        client.player.networkHandler.sendPacket(new HandSwingC2SPacket(hand));
    }
    public static void sendPosImmediately(double x, double y, double z, boolean onGround) {
        if (client.player == null) return;
        PlayerMoveC2SPacket packet = new PlayerMoveC2SPacket.PositionAndOnGround(x, y, z, client.player.isOnGround());
        sendImmediately(packet);
    }
    public static void sendImmediately(Packet<?> packet) {
        ((IClientConnection) Objects.requireNonNull(client.getNetworkHandler()).getConnection()).sendPacketImmediately(packet);
    }
    public static void send(Packet<?> packet) {
        if (MinecraftClient.getInstance().getNetworkHandler() == null) return;
        MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
    }
    public static void attackEntity(Entity ent) {
        if (client.player == null) return;
        client.player.resetLastAttackedTicks();
        PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(ent, client.player.isSneaking());
        send(packet);
    }
    public static void attackEntityImmediately(Entity ent) {
        if (client.player == null) return;
        client.player.resetLastAttackedTicks();
        PlayerInteractEntityC2SPacket packet = PlayerInteractEntityC2SPacket.attack(ent, client.player.isSneaking());
        sendImmediately(packet);
    }
}
