package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class UpdateBlockBreakingProgressData extends EventData {
    public BlockPos getBlockPos() {
        return getArg(0);
    }
    public Direction getDirection() {
        return getArg(1);
    }
    public UpdateBlockBreakingProgressData(BlockPos pos, Direction direction) {
        super(pos, direction);
    }
}
