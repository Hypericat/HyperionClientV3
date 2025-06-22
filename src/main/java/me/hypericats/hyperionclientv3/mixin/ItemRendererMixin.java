package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.modules.SmallHandRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
	@Inject(method = "renderItem(Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II[ILnet/minecraft/client/render/model/BakedModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderBakedItemModel(Lnet/minecraft/client/render/model/BakedModel;[IIILnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void onRenderItem(net.minecraft.item.ModelTransformationMode transformationMode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, int[] tints, BakedModel model, RenderLayer layer, ItemRenderState.Glint glint, CallbackInfo ci) {
		if (!transformationMode.isFirstPerson() || !ModuleHandler.isModuleEnable(SmallHandRenderer.class)) return;
		Vec3d offset = SmallHandRenderer.getOffsetVector();
		Vec3d scale = SmallHandRenderer.getScaleVector();

		matrices.translate(offset.getX(), offset.getY(), offset.getZ());
		matrices.scale((float) scale.getX(), (float) scale.getY(), (float) scale.getZ());
	}
}