package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.UpdateBlockBreakingProgressListener;
import me.hypericats.hyperionclientv3.events.eventData.UpdateBlockBreakingProgressData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientPlayerInteractionManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Shadow private int blockBreakingCooldown;
    @Shadow private float currentBreakingProgress;

    public void setBlockHitDelay(int delay) {
        this.blockBreakingCooldown = delay;
    }

    public float getCurrentBlockBreakingProgress() {
        return currentBreakingProgress;
    }

    @Inject(at = @At("HEAD"), method = "updateBlockBreakingProgress", cancellable = true)
    public void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        UpdateBlockBreakingProgressData data = new UpdateBlockBreakingProgressData(pos, direction);
        System.out.println("Progress!");
        EventHandler.onEvent(UpdateBlockBreakingProgressListener.class, data);

        if (data.isCancelled()) cir.cancel();
    }
}
