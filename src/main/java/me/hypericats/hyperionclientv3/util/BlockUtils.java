package me.hypericats.hyperionclientv3.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
    public static List<BlockPos> getBlockInRange(BlockPos pos, int range) {
        List<BlockPos> poses = new ArrayList<>();
        for (int z = -range; z <= range; z++) {
            poses.addAll(getVerticleSliceXY(pos.add(0, 0, z), range));
        }
        return poses;
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
}
