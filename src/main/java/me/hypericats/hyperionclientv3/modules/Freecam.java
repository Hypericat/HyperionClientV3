package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.IsFullCubeListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.IsFullCubeData;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class Freecam extends Module implements TickListener, SendPacketListener, IsFullCubeListener {
    public Freecam() {
        super(false);
    }
    private boolean noOverlayState;
    private FakePlayerEntity fakePlayer;
    private BooleanOption doOffGroundBypass;
    private BooleanOption blockSneaks;
    private BooleanOption blockLooks;
    private boolean isOnGround;
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
        if (data instanceof IsFullCubeData isFullCubeData) {
            isFullCubeData.setReturnValue(false);
            return;
        }
        flight.fly(client);
        if (!isOnGround && doOffGroundBypass.getValue()) flight.doBypass(PlayerUtils.getServerPosition(), client);
    }
    public VoxelShape getEntityVoxelShape(Entity entity, BlockState state, BlockView world, BlockPos pos) {
        if (MinecraftClient.getInstance().player.getId() == entity.getId() && this.isEnabled()) return VoxelShapes.empty();
        return state.getCollisionShape(world , pos);
    }
    private void handlePacketData(SendPacketData packetData) {
        Packet<?> packet = packetData.getPacket();
        Vec3d realPos = PlayerUtils.getServerPosition();
        if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround && blockLooks.getValue()) {
            packetData.cancel();
            return;
        }
        if (packet instanceof ClientCommandC2SPacket commandC2SPacket && blockSneaks.getValue()) {
            if (commandC2SPacket.getMode() == ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY || commandC2SPacket.getMode() == ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY) {
                packetData.cancel();;
                return;
            }
        }
        if (!(packet instanceof PlayerMoveC2SPacket movePacket)) return;
        Vec3d pos = new Vec3d(movePacket.getX(realPos.getX()), movePacket.getY(realPos.getY()), movePacket.getZ(realPos.getZ()));
        if (realPos.distanceTo(pos) > 0.1d) packetData.cancel();
    }
    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = new FakePlayerEntity();

        Module module = ModuleHandler.getModuleByClass(Flight.class);
        flight = (Flight) module;

        Module noOverlay = ModuleHandler.getModuleByClass(NoOverlay.class);
        if (noOverlay != null) {
            noOverlayState = noOverlay.isEnabled();
            noOverlay.enable(false);
        }


        isOnGround = client.player.isOnGround();

        EventHandler.register(TickListener.class, this);
        EventHandler.register(SendPacketListener.class, this);
        EventHandler.register(IsFullCubeListener.class, this);
    }

    @Override
    protected void initOptions() {
        doOffGroundBypass = new BooleanOption(true, "Bypass Mid Air Kick", true);
        blockLooks = new BooleanOption(true, "Block Look Packets", true);
        blockSneaks = new BooleanOption(true, "Block Sneak Packets", true);

        options.addOption(doOffGroundBypass);
        options.addOption(blockLooks);
        options.addOption(blockSneaks);
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
        EventHandler.unregister(IsFullCubeListener.class, this);

        if (fakePlayer != null) {
            fakePlayer.resetPlayerPosition();
            fakePlayer.despawn();
            fakePlayer = null;
        }

        Module noOverlay = ModuleHandler.getModuleByClass(NoOverlay.class);
        if (noOverlay != null) {
            noOverlay.setEnabled(noOverlayState, false);
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.setVelocity(Vec3d.ZERO);
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
