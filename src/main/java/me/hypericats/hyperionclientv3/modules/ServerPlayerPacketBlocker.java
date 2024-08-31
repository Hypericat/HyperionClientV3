package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.TeleportConfirmC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class ServerPlayerPacketBlocker extends Module implements RecievePacketListener {
    public ServerPlayerPacketBlocker() {
        super(true);
    }
    private BooleanOption blockLooks;
    private BooleanOption blockSmallMovements;
    private NumberOption<Double> smallMovementThreshHold;
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        RecievePacketData packetData = (RecievePacketData) data;
        Packet<?> packet = packetData.getPacket();
        if (!(packet instanceof PlayerPositionLookS2CPacket playerPositionLookS2CPacket)) return;
        Vec3d packetPos = new Vec3d(playerPositionLookS2CPacket.getX(), playerPositionLookS2CPacket.getY(), playerPositionLookS2CPacket.getZ());

        if (blockSmallMovements.getValue() && (packetPos.distanceTo(PlayerUtils.getServerPosition()) < smallMovementThreshHold.getValue()) || smallMovementThreshHold.getValue() < 0) {
            packetData.cancel();
            //pretend the teleport was accepted
            PacketUtil.send(new TeleportConfirmC2SPacket(playerPositionLookS2CPacket.getTeleportId()));
            return;
        }

        if (!blockLooks.getValue()) return;
       packetData.setNewPacket(new PlayerPositionLookS2CPacket(playerPositionLookS2CPacket.getX(), playerPositionLookS2CPacket.getY(), playerPositionLookS2CPacket.getZ(), client.player.getYaw(), client.player.getPitch(), playerPositionLookS2CPacket.getFlags(), playerPositionLookS2CPacket.getTeleportId()));

    }
    @Override
    public void onEnable() {
        EventHandler.register(RecievePacketListener.class, this);
    }

    @Override
    protected void initOptions() {
        blockLooks = new BooleanOption(true, "Block Look Packets", true);
        blockSmallMovements = new BooleanOption(true, "Block Small Move Packets", true);
        smallMovementThreshHold = new NumberOption<>(true, "Small Movement Threshold", 0.5d);

        options.addOption(blockLooks);
        options.addOption(blockSmallMovements);
        options.addOption(smallMovementThreshHold);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RecievePacketListener.class, this);
    }

    @Override
    public String getName() {
        return "PacketBlocker";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Look Reset", "Rubber Band", "Move", "Server"};
    }
}
