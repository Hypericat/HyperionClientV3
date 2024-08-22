package me.hypericats.hyperionclientv3.util;

import me.hypericats.hyperionclientv3.enums.PlayerInteractType;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class PlayerInteractEntityC2SPacketHandler implements PlayerInteractEntityC2SPacket.Handler {
    List<PlayerInteractType> typeRegister;
    public PlayerInteractEntityC2SPacketHandler(List<PlayerInteractType> typeRegister) {
        this.typeRegister = typeRegister;
    }
    @Override
    public void interact(Hand hand) {
        typeRegister.add(PlayerInteractType.INTERACT);
    }

    @Override
    public void interactAt(Hand hand, Vec3d pos) {
        typeRegister.add(PlayerInteractType.INTERACTAT);
    }

    @Override
    public void attack() {
        typeRegister.add(PlayerInteractType.ATTACK);
    }
}
