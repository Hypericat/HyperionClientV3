package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.modules.Freecam;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockCollisionSpliterator;
import net.minecraft.world.BlockView;
import net.minecraft.world.CollisionView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(BlockCollisionSpliterator.class)
public class BlockCollisionSpliteratorMixin {

    private PlayerEntity entity;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onConstructed(CollisionView world, Entity entity, Box box, boolean forEntity, BiFunction resultFunction, CallbackInfo ci) {
        if (entity instanceof PlayerEntity) {
            this.entity = (PlayerEntity) entity;
        }
    }

    @Redirect(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape onGetCollisionShape(BlockState blockState, BlockView world, BlockPos blockPos, ShapeContext context) {
        Freecam freecam = (Freecam) ModuleHandler.getModuleByClass(Freecam.class);
        if (entity == null || MinecraftClient.getInstance().player == null || freecam == null) return blockState.getCollisionShape(world, blockPos, context);
        return freecam.getEntityVoxelShape(entity, blockState, world, blockPos);
    }
}
