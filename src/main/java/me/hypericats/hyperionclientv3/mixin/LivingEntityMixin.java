package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.AddStatusEffectListener;
import me.hypericats.hyperionclientv3.events.KeyInputListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.AddStatusEffectData;
import me.hypericats.hyperionclientv3.events.eventData.KeyInputData;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import me.hypericats.hyperionclientv3.mixinInterface.ILivingEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntity {

	@Shadow protected abstract int getHandSwingDuration();

	public int getHandSwingingTicks() {
		return this.getHandSwingDuration();
	}

	@Inject(at = @At("HEAD"), method = {"addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z"}, cancellable = true)
	private static void addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		AddStatusEffectData data = new AddStatusEffectData(effect, source);
		EventHandler.onEvent(AddStatusEffectListener.class, data);
		if (data.isCancelled()) cir.setReturnValue(false);

	}
}