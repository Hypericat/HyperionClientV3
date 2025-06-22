package me.hypericats.hyperionclientv3.mixin;


import me.hypericats.hyperionclientv3.mixinInterface.IDrawContext;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DrawContext.class)
public class DrawContextMixin implements IDrawContext {

    @Shadow @Final private VertexConsumerProvider.Immediate vertexConsumers;

    @Override
    public VertexConsumerProvider getVertexConsumers() {
        return this.vertexConsumers;
    }
}
