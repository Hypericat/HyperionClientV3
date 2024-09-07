package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.ScheduleStopListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import me.hypericats.hyperionclientv3.modules.WaypointHandler;
import net.fabricmc.loader.impl.lib.tinyremapper.extension.mixin.MixinExtension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Util;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.SortedMap;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Shadow @Final private BufferBuilderStorage bufferBuilders;

	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilderStorage;getEntityVertexConsumers()Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;", shift = At.Shift.AFTER))
	private void onRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrices) {
		PreRenderEntityData preRenderEntityData = new PreRenderEntityData(matrices, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), this.bufferBuilders.getEntityVertexConsumers(), camera);
		EventHandler.onEvent(PreRenderEntityListener.class, preRenderEntityData);
	}
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderChunkDebugInfo(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/Camera;)V", shift = At.Shift.BEFORE))
	private void onPostRender(RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci, @Local MatrixStack matrices) {
		matrices.push();
		matrices.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
		PostRenderData postRenderData = new PostRenderData(matrices, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), this.bufferBuilders.getEntityVertexConsumers(), camera);
		EventHandler.onEvent(PostRenderListener.class, postRenderData);
		matrices.pop();
	}
}