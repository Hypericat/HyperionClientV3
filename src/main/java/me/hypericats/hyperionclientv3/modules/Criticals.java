package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.CriticalsType;
import me.hypericats.hyperionclientv3.enums.PlayerInteractType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerInteractEntityC2SPacketHandler;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Criticals extends Module implements SendPacketListener {

    EnumStringOption<CriticalsType> criticalsType;
    List<PlayerInteractType> typeRegister = new ArrayList<>();
    PlayerInteractEntityC2SPacketHandler handler = new PlayerInteractEntityC2SPacketHandler(typeRegister);

    public Criticals() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        SendPacketData packetData = (SendPacketData) data;
        Packet<?> packet = packetData.getPacket();
        if (!(packet instanceof PlayerInteractEntityC2SPacket)) return;
        typeRegister.clear();
        ((PlayerInteractEntityC2SPacket) packet).handle(handler);
        if (typeRegister.isEmpty() || typeRegister.get(0) != PlayerInteractType.ATTACK) return;
        crit(PlayerUtils.getServerPosition());
    }
    public void crit() {
        if (MinecraftClient.getInstance().player == null) return;
        crit(MinecraftClient.getInstance().player.getPos());
    }
    public void crit(Vec3d playerPos) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if(client.player.isTouchingWater() || client.player.isInLava()) return;
        switch (criticalsType.getValue()) {
            case PACKET -> doPacketJump(client, playerPos);
            case VELOCITY -> doVelocityJump(client);
        }
    }
    public void doPacketJump(MinecraftClient client, Vec3d playerPos) {
        double posX = playerPos.getX();
        double posY = playerPos.getY();
        double posZ = playerPos.getZ();

        PacketUtil.sendPos(posX, posY + 0.0625D, posZ, false);
        PacketUtil.sendPos(posX, posY, posZ, false);
        PacketUtil.sendPos(posX, posY + 1.1E-5D, posZ, false);
        PacketUtil.sendPos(posX, posY, posZ, false);
    }
    public void doVelocityJump(MinecraftClient client) {
        client.player.sendMessage(Text.of("This doesn't work yet criticals velocity jump."));
    }
    @Override
    public void onEnable() {
        EventHandler.register(SendPacketListener.class, this);
    }

    @Override
    protected void initOptions() {
        criticalsType = new EnumStringOption<>(true, "Criticals Type", CriticalsType.PACKET);

        options.addOption(criticalsType);
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(SendPacketListener.class, this);
    }

    @Override
    public String getName() {
        return "Criticals";
    }

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"Crit"};
    }
}
