package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements IClientConnection {


	@Shadow public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks);

	@Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

	@Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
	private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
		SendPacketData data = new SendPacketData(packet, callbacks);
		EventHandler.onEvent(SendPacketListener.class, data);
		if (!data.isCancelled()) return;
		ci.cancel();
		if (data.getNewPacket() != null) {
			this.send(data.getNewPacket(), callbacks);
		}
	}
	@Inject(at = @At("HEAD"), method = "handlePacket", cancellable = true)
	private static<T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo ci) {
		RecievePacketData data = new RecievePacketData(packet, listener);
		EventHandler.onEvent(RecievePacketListener.class, data);
		if (data.getNewPacket() != null) {
			ci.cancel();
			Packet<T> newPacket = (Packet<T>) data.getNewPacket();
			newPacket.apply((T) listener);
			return;
		}
		if (data.isCancelled()) ci.cancel();
	}

	public void sendPacketImmediately(Packet<?> packet) {
		sendImmediately(packet, null, false);
	}
}