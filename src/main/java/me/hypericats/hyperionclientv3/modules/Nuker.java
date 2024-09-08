package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.BlockUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Nuker extends Module implements TickListener, SendPacketListener {
    public Nuker() {
        super(false);
    }
    private NumberOption<Double> range;
    private NumberOption<Integer> maxPacketRate;
    int tickCounter = 0;
    private final int[] packetHistory = new int[20];

    @Override
    public void onEvent(EventData data) {
        if (data instanceof SendPacketData) {
            incrementPacketCounter();
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();

        tickCounter ++;
        if (tickCounter >= 20) tickCounter = 0;
        packetHistory[tickCounter] = 0;

        if (client.world == null) return;
        if (client.player == null) return;
        if (client.interactionManager == null) return;
        if (!(client.crosshairTarget instanceof BlockHitResult blockHitResult)) return;

        BlockPos center = blockHitResult.getBlockPos();
        if (!BlockUtils.canBeClicked(center, client)) return;
        List<BlockPos> toMine = BlockUtils.getBlockInRange(center, range.getValue().intValue(), value -> !value[0].equals(center) && BlockUtils.canBeClicked(value[0], client) && (BlockUtils.getHardness(value[0], client) > 0 || client.player.isCreative()));
        int maxPacket = maxPacketRate.getValue() == -1 ? Integer.MAX_VALUE : maxPacketRate.getValue();

        if (client.player.isCreative()) {
            for (BlockPos pos : toMine) {
                if (getTotalPackets() >= maxPacket) return;
                BlockUtils.packetBreakBlock(pos, client);
                incrementPacketCounter(2);
            }
            if (getTotalPackets() >= maxPacket) return;
            BlockUtils.packetBreakBlock(center, client);
            incrementPacketCounter(2);
            return;
        }


        //Filter for instant blocks

        for (int i = 0; i < toMine.size(); i++) {
            BlockPos block = toMine.get(i);
            if (getTotalPackets() >= maxPacket) return;
            if (BlockUtils.getHardness(block, client) >= 1) {
                BlockUtils.packetBreakBlock(block, client);
                incrementPacketCounter(2);
                toMine.remove(i);
                i--;
            }
        }
        float centerHardness = BlockUtils.getHardness(center, client);
        if (centerHardness >= 1) {
            if (getTotalPackets() >= maxPacket) return;
            BlockUtils.packetBreakBlock(center, client);
            incrementPacketCounter(2);
            return;
        }
        if (getTotalPackets() >= maxPacket) return;


        if (toMine.isEmpty()) {
            if (centerHardness <= 0) return;
            toMine.add(center);
        }
        BlockPos currentBlock = getQuickestBlock(toMine, client);

        client.interactionManager.updateBlockBreakingProgress(currentBlock, BlockUtils.getBlockSide(currentBlock, client));
        incrementPacketCounter(2);
    }
    public BlockPos getQuickestBlock(List<BlockPos> pos, MinecraftClient client) {
        double bestHardness = Double.MIN_VALUE;
        BlockPos blockPos = pos.getFirst();
        double currentHardness;
        for (BlockPos block : pos) {
            currentHardness = BlockUtils.getHardness(block, client);
            if (currentHardness > bestHardness) {
                bestHardness = currentHardness;
                blockPos = block;
            }
        }
        return blockPos;
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
        EventHandler.register(SendPacketListener.class, this);
    }
    private int getTotalPackets() {
        return Arrays.stream(packetHistory).sum();
    }
    private void incrementPacketCounter() {
        incrementPacketCounter(1);
    }
    private void incrementPacketCounter(int value) {
        packetHistory[tickCounter] += value;
    }

    @Override
    protected void initOptions() {
        range = new NumberOption<>(true, "Range", 2d);
        maxPacketRate = new NumberOption<>(true, "Max Packet Rate", 450);

        options.addOption(range);
        options.addOption(maxPacketRate);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(SendPacketListener.class, this);
    }

    @Override
    public String getName() {
        return "Nuker";
    }

    @Override
    public HackType getHackType() {
        return HackType.OTHER;
    }

    public String[] getAlias() {
        return new String[] {"Speed Mine", "Speed Nuker"};
    }
}
