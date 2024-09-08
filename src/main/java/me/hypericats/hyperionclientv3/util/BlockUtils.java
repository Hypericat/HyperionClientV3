package me.hypericats.hyperionclientv3.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    public static List<BlockPos> getBlockInRange(BlockPos pos, int range, Condition<BlockPos> condition) {
        List<BlockPos> poses = new ArrayList<>();
        for (int z = -range; z <= range; z++) {
            poses.addAll(getVerticleSliceXY(pos.add(0, 0, z), range));
        }

        if (condition != null) {
            for (int i = 0; i < poses.size(); i++) {
                BlockPos value = poses.get(i);
                if (!condition.apply(value)) {
                    poses.remove(i);
                    i--;
                }
            }
        }
        return poses;
    }
    public static List<BlockPos> getBlockInRange(BlockPos pos, int range) {
        return getBlockInRange(pos, range, null);
    }
    public static List<BlockPos> getVerticleSliceXY(BlockPos pos, int range) {
        List<BlockPos> poses = new ArrayList<>();
        for (int y = -range; y <= range; y++) {
            poses.addAll(getSliceCordX(pos.add(0, y, 0), range));
        }
        return poses;
    }
    public static List<BlockPos> getSliceCordX(BlockPos pos, int range) {
        List<BlockPos> poses = new ArrayList<>();
        for (int x = -range; x <= range; x++) {
            poses.add(pos.add(x, 0, 0));
        }
        return poses;
    }
    public static boolean blockStateCompare(BlockState state, Block block) {
        return state.getBlock() == block;
    }
    public static void packetBreakBlocks(Iterable<BlockPos> blocks, MinecraftClient client) {
        for(BlockPos pos : blocks)
        {
            packetBreakBlock(pos, client);
        }
    }
    public static void packetBreakBlock(BlockPos block, MinecraftClient client) {
        Direction side = getBlockSide(block, client);
        PacketUtil.sendImmediately(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, block, side));
        PacketUtil.sendImmediately(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, block, side));
    }
    public static Direction getBlockSide(BlockPos block, MinecraftClient client) {
        Vec3d posVec = Vec3d.ofCenter(block);
        double distanceSqPosVec = client.player.getEyePos().squaredDistanceTo(posVec);

        for(Direction side : Direction.values())
        {
            Vec3d hitVec = posVec.add(Vec3d.of(side.getVector()).multiply(0.5));
            if(client.player.getEyePos().squaredDistanceTo(hitVec) >= distanceSqPosVec) continue;
            return side;
        }
        return Direction.UP;
    }
    public static float getHardness(BlockPos pos, MinecraftClient client) {
        return client.world.getBlockState(pos).calcBlockBreakingDelta(client.player, client.world, pos);
    }
    public static boolean canBeClicked(BlockPos pos, MinecraftClient client) {
        return client.world.getBlockState(pos).getOutlineShape(client.world, pos) != VoxelShapes.empty();
    }

    public static List<WorldChunk> getLoadedChunks(MinecraftClient client) {
        List<WorldChunk> chunks = new ArrayList<>();
        if (client.world == null) return chunks;
        if (client.player == null) return chunks;

        int viewDistance = client.options.getClampedViewDistance() + 2;
        ChunkPos center = client.player.getChunkPos();
        for (ChunkPos chunkX : getXChunks(viewDistance, center)) {
            for (ChunkPos chunkPos : getZChunks(viewDistance, chunkX)) {
                if (client.world.isChunkLoaded(chunkPos.x, chunkPos.z)) {
                    WorldChunk chunk = client.world.getChunk(chunkPos.x, chunkPos.z);
                    if (chunk == null) continue;
                    chunks.add(chunk);
                }
            }
        }
        return chunks;
    }
    public static List<ChunkPos> getXChunks(int radius, ChunkPos center) {
        List<ChunkPos> chunks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            chunks.add(new ChunkPos(center.x + x, center.z));
        }
        return chunks;
    }
    public static List<ChunkPos> getZChunks(int radius, ChunkPos center) {
        List<ChunkPos> chunks = new ArrayList<>();
        for (int z = -radius; z <= radius; z++) {
            chunks.add(new ChunkPos(center.x, center.z + z));
        }
        return chunks;
    }
}
