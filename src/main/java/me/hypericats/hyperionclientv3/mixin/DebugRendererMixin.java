package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Inject(at = @At("HEAD"), method = {"render"})
    private void onRenderDebug(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        matrices.push();
        matrices.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
        PostRenderData postRenderData = new PostRenderData(matrices, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), vertexConsumers, MinecraftClient.getInstance().gameRenderer.getCamera());
        EventHandler.onEvent(PostRenderListener.class, postRenderData);
        matrices.pop();
    }
}
