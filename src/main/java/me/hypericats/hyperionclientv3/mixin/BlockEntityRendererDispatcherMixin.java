package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.BlockEntityRendererListener;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.eventData.BlockEntityRendererData;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRendererDispatcherMixin {

	@Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("TAIL"), cancellable = true)
	private static <T extends BlockEntity> void onRender(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		BlockEntityRendererData data = new BlockEntityRendererData(renderer, blockEntity, tickDelta, matrices, vertexConsumers);
		EventHandler.onEvent(BlockEntityRendererListener.class, data);
		if (data.isCancelled()) ci.cancel();
	}
}