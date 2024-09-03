package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.DisconnectListener;
import me.hypericats.hyperionclientv3.events.PostJoinWorldListener;
import me.hypericats.hyperionclientv3.events.ScheduleStopListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.DisconnectData;
import me.hypericats.hyperionclientv3.events.eventData.PostJoinWorldData;
import me.hypericats.hyperionclientv3.mixinInterface.IMinecraftClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements IMinecraftClient {
	@Shadow private int itemUseCooldown;

	@Inject(at = @At("HEAD"), method = "tick")
	private void onTick(CallbackInfo ci) {
		EventHandler.onEvent(TickListener.class, new EventData());
	}

	@Inject(at = @At("HEAD"), method = "run")
	private void onRun(CallbackInfo ci) {
		HyperionClientV3Client.actuallyInit();
	}

	@Inject(at = @At("HEAD"), method = "scheduleStop")
	private void onScheduleStop(CallbackInfo ci) {
		EventHandler.onEvent(ScheduleStopListener.class, new EventData());
	}
	@Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
	private void onDisconnect(Screen screen, CallbackInfo ci) {
		EventHandler.onEvent(DisconnectListener.class, new DisconnectData(screen));
	}
	public void setItemUseCooldown(int cooldown) {
		this.itemUseCooldown = cooldown;
	}
	public int getItemUseCooldown() {
		return this.itemUseCooldown;
	}
}