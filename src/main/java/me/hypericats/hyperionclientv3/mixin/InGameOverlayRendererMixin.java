package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameOverlayRendererListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameOverlayRendererData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {
    @Inject(at = @At("HEAD"), method = {"renderOverlays(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V"}, cancellable = true)
    private static void renderOverlaysWrapper(MinecraftClient world, MatrixStack pos, CallbackInfo ci) {
        InGameOverlayRendererData data = new InGameOverlayRendererData(world, pos);
        EventHandler.onEvent(InGameOverlayRendererListener.class, data);
        if (data.isCancelled()) ci.cancel();
    }
}