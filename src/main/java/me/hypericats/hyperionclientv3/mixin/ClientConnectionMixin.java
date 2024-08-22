package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements IClientConnection {

	@Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks);

	@Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
	private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
		SendPacketData data = new SendPacketData(packet, callbacks);
		EventHandler.onEvent(SendPacketListener.class, data);
		if (data.isCancelled()) ci.cancel();
	}

	public void sendPacketImmediately(Packet<?> packet) {
		sendImmediately(packet, null);
	}
}