package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ClientPlayerMoveListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.ClientPlayerMovementData;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	private Vec3d newMovement;
	@Inject(at = @At("HEAD"), method = "move", cancellable = true)
	private void onMove(MovementType movementType, Vec3d movement, CallbackInfo ci) {
		ClientPlayerMovementData data = new ClientPlayerMovementData(movementType, movement);
		EventHandler.onEvent(ClientPlayerMoveListener.class, data);
		if (data.isCancelled()) ci.cancel();
		if (data.getNewMovement() != null) this.newMovement = data.getNewMovement();
	}
	@ModifyArg(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), index = 1)
	private Vec3d onMove(Vec3d movement) {
		Vec3d newMovement = this.newMovement == null ? movement : this.newMovement;
		if (this.newMovement != null) this.newMovement = null;
		return newMovement;
	}
}