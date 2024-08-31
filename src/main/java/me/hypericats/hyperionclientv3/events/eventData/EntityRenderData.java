package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EntityRenderData extends EventData {
    public Entity getEntity() {
        return getArg(0);
    }
    public float getYaw() {return getArg(1);}
    public float getTickDelta() {return getArg(2);}
    public MatrixStack getMatrices() {return getArg(3);}
    public VertexConsumerProvider getVertexConsumer() {return getArg(4);}
    public int getLight() {return getArg(5);}
    public EntityRenderData(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }
}
