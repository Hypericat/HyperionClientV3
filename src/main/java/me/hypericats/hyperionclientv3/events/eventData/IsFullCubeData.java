package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class IsFullCubeData extends EventData {
    private boolean returnValue;
    public void setReturnValue(boolean returnValue) {
        this.cancel();
        this.returnValue = returnValue;
    }
    public BlockPos getPos() {
        return getArg(1);
    }
    public BlockView getWorld() {
        return getArg(0);
    }
    public boolean getReturnValue() {
        return returnValue;
    }
    public IsFullCubeData(BlockView world, BlockPos pos) {
        super(world, pos);
    }
}
