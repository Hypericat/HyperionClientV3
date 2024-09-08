package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ClientPlayerMoveListener;
import me.hypericats.hyperionclientv3.events.eventData.ClientPlayerMovementData;
import me.hypericats.hyperionclientv3.modules.InvPortal;
import me.hypericats.hyperionclientv3.modules.NoSlow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.util.math.Vec3d;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
	@Shadow public abstract boolean isUsingItem();

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

	//noslow
	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
	private boolean isUsingItem(ClientPlayerEntity instance) {
		return !ModuleHandler.isModuleEnable(NoSlow.class) && isUsingItem();
	}
	@Redirect(method = "canStartSprinting", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
	private boolean canSprint(ClientPlayerEntity instance) {
		return !ModuleHandler.isModuleEnable(NoSlow.class) && isUsingItem();
	}
	@Redirect(method = "canSprint", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;getFoodLevel()I"))
	private int getFoodLevel(HungerManager instance) {
		return ModuleHandler.isModuleEnable(NoSlow.class) ? 7 : instance.getFoodLevel();
	}

	//Portalinv
	@Redirect(method = "tickNausea", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", ordinal = 0, opcode = Opcodes.GETFIELD))
	private Screen getCurrentScreen(MinecraftClient instance) {
		return ModuleHandler.isModuleEnable(InvPortal.class) ? null : instance.currentScreen;
	}
}