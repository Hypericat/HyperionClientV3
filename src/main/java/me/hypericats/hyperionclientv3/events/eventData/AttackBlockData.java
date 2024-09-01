package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.block.Block;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AttackBlockData extends EventData {
    public BlockPos getBlockPos() {
        return getArg(0);
    }
    public Direction getDirection() {return getArg(1);}
    public AttackBlockData(BlockPos blockPos, Direction direction) {
        super(blockPos, direction);
    }
}
