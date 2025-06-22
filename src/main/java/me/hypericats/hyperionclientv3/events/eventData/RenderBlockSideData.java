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
    public Direction getDir() {
        return getArg(2);
    }
    public BlockPos getOtherBlockState() {
        return getArg(1);
    }

    public RenderBlockSideData(BlockState state, BlockState otherState, Direction side) {
        super(state, otherState, side);
    }
}
