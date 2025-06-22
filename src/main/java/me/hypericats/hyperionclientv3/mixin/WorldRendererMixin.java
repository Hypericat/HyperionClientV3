package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow @Final private BufferBuilderStorage bufferBuilders;

	// This doesn't work but isin't called so I don't know where to put this

	//@Inject(method = "renderMain", at = @At(value = "INVOKE", target = "setR", shift = At.Shift.AFTER))
	//private void onRender(FrameGraphBuilder frameGraphBuilder, Frustum frustum, Camera camera, Matrix4f positionMatrix, Matrix4f projectionMatrix, Fog fog, boolean renderBlockOutline, boolean renderEntityOutlines, RenderTickCounter renderTickCounter, Profiler profiler, CallbackInfo ci, @Local MatrixStack matrices) {
	//	PreRenderEntityData preRenderEntityData = new PreRenderEntityData(matrices, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), this.bufferBuilders.getEntityVertexConsumers(), camera);
	//	EventHandler.onEvent(PreRenderEntityListener.class, preRenderEntityData);
	//}

	//@Inject(method = "renderMain", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V", shift = At.Shift.BEFORE))
	//private void onPostRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrices) {
	//	matrices.push();
	//	matrices.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
	//	PostRenderData postRenderData = new PostRenderData(matrices, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), this.bufferBuilders.getEntityVertexConsumers(), camera);
	//	EventHandler.onEvent(PostRenderListener.class, postRenderData);
	//	matrices.pop();
	//}
}