package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.modules.ForceRenderBarriers;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "getBlockParticle", at = @At("HEAD"), cancellable = true)
    public void onGetBlockParticle(CallbackInfoReturnable<Block> cir) {
        if (ModuleHandler.isModuleEnable(ForceRenderBarriers.class)) cir.setReturnValue(Blocks.BARRIER);
    }
}
