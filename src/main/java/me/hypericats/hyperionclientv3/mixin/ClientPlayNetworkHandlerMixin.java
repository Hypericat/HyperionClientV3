package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.commands.CommandHandlerDispatcher;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	private void onSendChatMessage(String content, CallbackInfo ci) {
		if (content.startsWith(CommandHandlerDispatcher.getCommandNominator())) {
			ci.cancel();
			CommandHandlerDispatcher.handleCommand(content);
		}
	}
}