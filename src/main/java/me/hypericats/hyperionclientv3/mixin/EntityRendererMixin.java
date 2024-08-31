package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderEntityListener;
import me.hypericats.hyperionclientv3.events.eventData.EntityRenderData;
import me.hypericats.hyperionclientv3.modules.PlayerEsp;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(at = @At(value = "HEAD"), method = "render", cancellable = true)
    private void onRender(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {
        EntityRenderData data = new EntityRenderData(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        EventHandler.onEvent(RenderEntityListener.class, data);
        if (data.isCancelled()) ci.cancel();
    }
    @Inject(at = @At(value = "HEAD"), method = "renderLabelIfPresent", cancellable = true)
    private void onRenderLabel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci)
    {

    }

    //remove length requirement of 64
    @Redirect(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;getSquaredDistanceToCamera(Lnet/minecraft/entity/Entity;)D"))
    private double getSquaredDistance(EntityRenderDispatcher instance, Entity entity) {
        return PlayerEsp.getAlwaysRenderPlayerNameStatus() ? 0d : instance.getSquaredDistanceToCamera(entity);
    }

    //remove name not rendering if player is crouching
    @Redirect(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaky()Z"))
    private boolean getCrouchStatus(Entity instance) {
        return PlayerEsp.getAlwaysRenderPlayerNameStatus() ? false : instance.isSneaky();
    }
    //make background invisible
    @ModifyArg(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"), index = 8)
    private int getColor(int color) {
        return PlayerEsp.getAlwaysRenderPlayerNameStatus() ? 0 : color;
    }
    //make text render through blocks
    @ModifyArg(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal = 1), index = 7)
    private TextRenderer.TextLayerType getRenderLayer(TextRenderer.TextLayerType layerType) {
        return PlayerEsp.getAlwaysRenderPlayerNameStatus() ? TextRenderer.TextLayerType.SEE_THROUGH : layerType;
    }

}
