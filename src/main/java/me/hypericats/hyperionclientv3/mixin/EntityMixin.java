package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.AddStatusEffectListener;
import me.hypericats.hyperionclientv3.events.GetHitboxListener;
import me.hypericats.hyperionclientv3.events.eventData.AddStatusEffectData;
import me.hypericats.hyperionclientv3.events.eventData.GetHitboxData;
import me.hypericats.hyperionclientv3.mixinInterface.IEntity;
import me.hypericats.hyperionclientv3.mixinInterface.ILivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

	@Shadow private Box boundingBox;


	@Shadow protected abstract Vec3d adjustMovementForCollisions(Vec3d movement);

	@Inject(at = @At("HEAD"), method = {"getBoundingBox"}, cancellable = true)
	private void getBoundingBox(CallbackInfoReturnable<Box> cir) {
		GetHitboxData data = new GetHitboxData((Entity) (Object) this, this.boundingBox);
		EventHandler.onEvent(GetHitboxListener.class, data);

		Box newBox = data.getNewBox();
		if (newBox != null) {
			cir.setReturnValue(newBox);
		}
	}
	public Box getRealBoundingBox() {
		return this.boundingBox;
	}

	public Vec3d adjustMovementCollision(Vec3d movement) {
		return this.adjustMovementForCollisions(movement);
	}
}