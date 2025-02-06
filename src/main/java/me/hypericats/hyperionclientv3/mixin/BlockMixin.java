package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderBlockSideListener;
import me.hypericats.hyperionclientv3.events.eventData.RenderBlockSideData;
import me.hypericats.hyperionclientv3.modules.NoBlockParticles;
import me.hypericats.hyperionclientv3.modules.NoSlow;
import me.hypericats.hyperionclientv3.util.NullBool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
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

    @Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
    private static void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, BlockPos otherPos, CallbackInfoReturnable<Boolean> cir) {

        RenderBlockSideData data = new RenderBlockSideData(state, world, pos, side, otherPos);
        EventHandler.onEvent(RenderBlockSideListener.class, data);

        NullBool cancel = data.isCancelledAsNullBool();
        // No effect on cir
        if (cancel == NullBool.NULL) return;

        // Cancel if I dont want to render
        cir.setReturnValue(!cancel.toBool());
    }

    @Inject(at = @At("HEAD"), method = {"spawnBreakParticles"}, cancellable = true)
    private void onSpawnBreakParticles(World world, PlayerEntity player, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (ModuleHandler.isModuleEnable(NoBlockParticles.class)) ci.cancel();
    }
}
