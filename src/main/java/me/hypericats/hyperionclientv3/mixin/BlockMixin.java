package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.InGameHudRenderListener;
import me.hypericats.hyperionclientv3.events.eventData.InGameHudRenderData;
import me.hypericats.hyperionclientv3.modules.NoSlow;
import net.minecraft.block.Block;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "getVelocityMultiplier", cancellable = true)
    private void onGetVelocityMultiplier(CallbackInfoReturnable<Float> cir) {
        if (!ModuleHandler.isModuleEnable(NoSlow.class)) return;
        cir.setReturnValue(Math.max(cir.getReturnValueF(), 1f));
    }
}
