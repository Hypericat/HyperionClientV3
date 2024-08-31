package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.events.RenderListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameHudRenderData;
import me.hypericats.hyperionclientv3.events.eventData.RenderData;
import me.hypericats.hyperionclientv3.modules.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRenderMixin {
    @Inject(at = @At(value = "RETURN", ordinal = 1), method = {"getFov(Lnet/minecraft/client/render/Camera;FZ)D"}, cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        Zoom zoom = (Zoom) ModuleHandler.getModuleByClass(Zoom.class);
        if (zoom == null) return;
        if (zoom.getKey() == null || (!(InputUtil.isKeyPressed(client.getWindow().getHandle(), zoom.getKey().getCode())) || client.currentScreen != null)) {
            zoom.disable(client);
            return;
        }
        zoom.enable(client);
        cir.setReturnValue(zoom.onGetFov(cir.getReturnValueD(), client));
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
