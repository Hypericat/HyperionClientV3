package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.events.RenderListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameHudRenderData;
import me.hypericats.hyperionclientv3.events.eventData.RenderData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRenderMixin {
    @Inject(at = @At(value = "HEAD"), method = "renderHand", cancellable = true)
    private void onRender(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci)
    {
        //RenderData data = new RenderData(matrices, camera, tickDelta);
        //EventHandler.onEvent(RenderListener.class, data);
        //if (data.isCancelled()) ci.cancel();
    }
    @Inject(
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
                    opcode = Opcodes.GETFIELD,
                    ordinal = 0),
            method = "renderWorld", cancellable = true)
    private void onRenderWorldHandRendering(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo ci)
    {
        RenderData data = new RenderData(matrices, tickDelta);
        EventHandler.onEvent(RenderListener.class, data);
        if (data.isCancelled()) ci.cancel();
    }
}
