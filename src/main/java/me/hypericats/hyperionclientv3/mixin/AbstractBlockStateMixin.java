package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.IsFullCubeListener;
import me.hypericats.hyperionclientv3.events.eventData.IsFullCubeData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class AbstractBlockStateMixin {

	@Inject(at = @At("TAIL"),
			method = {"isFullCube(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"}, cancellable = true)
	private void onIsFullCube(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir)
	{
		IsFullCubeData data = new IsFullCubeData(world, pos);
		EventHandler.onEvent(IsFullCubeListener.class, data);
		if (data.isCancelled()) cir.setReturnValue(data.getReturnValue());
	}
}