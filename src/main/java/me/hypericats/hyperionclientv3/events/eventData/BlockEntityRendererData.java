package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class BlockEntityRendererData extends EventData {
    public <T extends BlockEntity> BlockEntityRenderer<T> getRenderer() {
        return getArg(0);
    }

    public <T extends BlockEntity> T getBlockEntity() {
        return getArg(1);
    }

    public float getTickDelta() {
        return getArg(2);
    }

    public MatrixStack getMatrices() {
        return getArg(3);
    }

    public VertexConsumerProvider getVertexConsumers() {
        return getArg(4);
    }

    public <T extends BlockEntity> BlockEntityRendererData(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
        super(renderer, blockEntity, tickDelta, matrices, vertexConsumers);
    }
}
