package me.hypericats.hyperionclientv3.mixin;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NarratorManager.class)
public class NarratorManagerMixin {
    @Shadow @Final private Narrator narrator;

    @Shadow @Final private MinecraftClient client;

    //FUCK OFF NARRATOR
    @Inject(at = @At(value = "HEAD"), method = "isActive", cancellable = true)
    private void fuckOffNarrator(CallbackInfoReturnable<Boolean> cir)
    {
        cir.setReturnValue(false);

        //JUST TO BE SURE FUCK OFF NARRATOR
        client.options.getNarrator().setValue(NarratorMode.OFF);
    }

}
