package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public class RenderHandData extends EventData {
    public MatrixStack getMatrices() {
        return getArg(0);
    }
    public float getTickDelta() {
        return getArg(1);
    }
    public VertexConsumerProvider getVertexConsumer() {
        return getArg(2);
    }
    public RenderHandData(MatrixStack matrices, float tickDelta, VertexConsumerProvider vertexConsumerProvider) {
        super(matrices, tickDelta, vertexConsumerProvider);
    }
}
