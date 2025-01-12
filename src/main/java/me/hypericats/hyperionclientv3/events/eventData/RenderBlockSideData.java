package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class RenderBlockSideData extends EventData {
    public BlockState getBlockState() {
        return getArg(0);
    }
    public BlockView getWorld() {
        return getArg(1);
    }
    public BlockPos getBlockPos() {
        return getArg(2);
    }
    public Direction getDir() {
        return getArg(3);
    }
    public BlockPos getOtherBlockPos() {
        return getArg(4);
    }

    public RenderBlockSideData(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos) {
        super(state, world, pos, side, otherPos);
    }
}
