package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.commands.CommandHandlerDispatcher;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostJoinWorldListener;
import me.hypericats.hyperionclientv3.events.PreJoinWorldListener;
import me.hypericats.hyperionclientv3.events.eventData.PostJoinWorldData;
import me.hypericats.hyperionclientv3.events.eventData.PreJoinWorldData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.play.ResourcePackStatusC2SPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow private ClientWorld world;

	@Shadow @Final private MinecraftClient client;

	@Shadow @Final private @Nullable ServerInfo serverInfo;

	@Shadow protected abstract void sendResourcePackStatus(ResourcePackStatusC2SPacket.Status packStatus);

	@Shadow protected abstract void feedbackAfterDownload(CompletableFuture<?> downloadFuture);

	@Shadow @Final private ClientConnection connection;

	@Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
	private void onSendChatMessage(String content, CallbackInfo ci) {
		if (content.startsWith(CommandHandlerDispatcher.getCommandNominator())) {
			ci.cancel();
			CommandHandlerDispatcher.handleCommand(content);
		}
	}
	@Inject(method = "onGameJoin", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/debug/DebugRenderer;reset()V"))
	private void onGameJoinNullPlayer(CallbackInfo ci) {
		EventHandler.onEvent(PostJoinWorldListener.class, new PostJoinWorldData(this.world));
	}
	@Inject(method = "onGameJoin", at = @At(value = "HEAD"))
	private void onPreGameJoin(CallbackInfo ci) {
		EventHandler.onEvent(PreJoinWorldListener.class, new PreJoinWorldData(this.world));
	}

	@Inject(method = "onPlayerRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;)V", shift = At.Shift.AFTER))
	private void onPlayerRespawn(CallbackInfo ci) {
		EventHandler.onEvent(PostJoinWorldListener.class, new PostJoinWorldData(this.world));
	}
	@Inject(method = "onPlayerRespawn", at = @At(value = "HEAD"))
	private void onPrePlayerRespawn(CallbackInfo ci) {
		EventHandler.onEvent(PreJoinWorldListener.class, new PreJoinWorldData(this.world));
	}
}