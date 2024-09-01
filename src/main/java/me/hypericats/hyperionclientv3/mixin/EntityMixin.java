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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

	@Shadow private Box boundingBox;

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
}