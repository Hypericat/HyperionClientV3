package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Module implements TickListener, SendPacketListener {
    public Freecam() {
        super(false);
    }
    private FakePlayerEntity fakePlayer;
    private Flight flight;

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        if (data instanceof SendPacketData packetData) {
            handlePacketData(packetData);
            return;
        }
        flight.fly();
    }
    public void handlePacketData(SendPacketData packetData) {
        Packet<?> packet = packetData.getPacket();
        Vec3d realPos = PlayerUtils.getAttackPlayerPosition();
        if (!(packet instanceof PlayerMoveC2SPacket movePacket)) return;
        Vec3d pos = new Vec3d(movePacket.getX(realPos.getX()), movePacket.getY(realPos.getY()), movePacket.getZ(realPos.getZ()));
        if (realPos.distanceTo(pos) > 0.1d) packetData.cancel();
    }
    @Override
    public void onEnable() {
        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = new FakePlayerEntity();

        Module module = ModuleHandler.getModuleByClass(Flight.class);
        flight = (Flight) module;

        EventHandler.register(TickListener.class, this);
        EventHandler.register(SendPacketListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    public Vec3d getFakePlayerPosition() {
        if (fakePlayer != null)
            return fakePlayer.getPos();
        return MinecraftClient.getInstance().player.getPos();
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(SendPacketListener.class, this);

        if (fakePlayer != null) {
            fakePlayer.resetPlayerPosition();
            fakePlayer.despawn();
            fakePlayer = null;
        }
    }

    @Override
    public String getName() {
        return "Freecam";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Noclip", "Spectator"};
    }
}
