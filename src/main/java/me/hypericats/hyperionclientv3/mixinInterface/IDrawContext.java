package me.hypericats.hyperionclientv3.mixinInterface;

import net.minecraft.client.render.VertexConsumerProvider;

public interface IDrawContext {
    VertexConsumerProvider getVertexConsumers();
}
