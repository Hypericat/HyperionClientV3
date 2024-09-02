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
import me.hypericats.hyperionclientv3.util.ChatUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class Blink extends Module implements TickListener, SendPacketListener {
    public Blink() {
        super(true);
    }
    private final List<Packet<?>> packetList = new ArrayList<>();
    private FakePlayerEntity fakePlayer;
    private boolean isOnGround;
    private BooleanOption doOffGroundBypass;

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        if (data instanceof SendPacketData packetData) {
            handlePacketData(packetData);
            return;
        }

        Flight flight = (Flight) ModuleHandler.getModuleByClass(Flight.class);
        if (flight == null) return;
        if (!isOnGround && doOffGroundBypass.getValue()) flight.doBypass(PlayerUtils.getServerPosition(), client);
    }
    private void handlePacketData(SendPacketData packetData) {
        Packet<?> packet = packetData.getPacket();
        if (packet instanceof ClientStatusC2SPacket) return;
        packetList.add(packet);
        packetData.cancel();
    }
    @Override
    public void onEnable() {
        if (MinecraftClient.getInstance().isInSingleplayer()) {
            ChatUtils.sendError(this.getName() + " does not work in singlePlayer, it will not work while in this world!");
            this.disable();
            return;
        }
        EventHandler.register(TickListener.class, this);
        EventHandler.register(SendPacketListener.class, this);

        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = new FakePlayerEntity();
        packetList.clear();

        if (MinecraftClient.getInstance().player == null) return;
        isOnGround = MinecraftClient.getInstance().player.isOnGround();
    }

    @Override
    protected void initOptions() {
        doOffGroundBypass = new BooleanOption(true, "Bypass Mid Air Kick", true);

        options.addOption(doOffGroundBypass);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(SendPacketListener.class, this);

        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = null;

        for (Packet<?> packet : packetList) {
            PacketUtil.send(packet);
        }
        packetList.clear();
    }

    @Override
    public String getName() {
        return "Blink";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Lag Switch", "Lag", "Internet"};
    }
}
