package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.IsFullCubeListener;
import me.hypericats.hyperionclientv3.events.MouseScrollListener;
import me.hypericats.hyperionclientv3.events.eventData.IsFullCubeData;
import me.hypericats.hyperionclientv3.events.eventData.MouseScrollData;
import net.minecraft.block.AbstractBlock;
import net.minecraft.client.Mouse;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mouse.class)
public class MouseMixin {

	@Inject(at = @At("HEAD"), method = {"onMouseScroll"}, cancellable = true)
	private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci)
	{
		MouseScrollData data = new MouseScrollData(window, horizontal, vertical);
		EventHandler.onEvent(MouseScrollListener.class, data);
		if (data.isCancelled()) ci.cancel();
	}
}