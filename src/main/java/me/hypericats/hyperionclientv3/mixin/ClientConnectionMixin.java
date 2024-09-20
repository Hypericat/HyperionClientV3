package me.hypericats.hyperionclientv3.mixin;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
import net.minecraft.network.packet.c2s.config.SelectKnownPacksC2SPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.Charset;
import java.util.Arrays;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin implements IClientConnection {


	@Shadow public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks);

	@Shadow protected abstract void sendImmediately(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

	@Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", cancellable = true)
	private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
		SendPacketData data = new SendPacketData(packet, callbacks);
		if (data.getPacket() instanceof SelectKnownPacksC2SPacket pack) {
			ByteBuf buf = Unpooled.buffer();
			SelectKnownPacksC2SPacket.CODEC.encode(buf, pack);
			System.out.println("Found selectKnownPacks: ");
			debugBuf(buf);


		}
		EventHandler.onEvent(SendPacketListener.class, data);
		if (!data.isCancelled()) return;
		ci.cancel();
		if (data.getNewPacket() != null) {
			this.send(data.getNewPacket(), callbacks);
		}
	}

	private static void debugBuf(ByteBuf buf) {
		int readIndex = buf.readerIndex();;
		byte[] byteArray = new byte[buf.readableBytes()];
		buf.getBytes(readIndex, byteArray);
		buf.readerIndex(readIndex);
		debugBuf(byteArray);
		System.out.println(new String(byteArray, Charset.defaultCharset()));
	}
	private static void debugBuf(byte[] bytes) {
		System.out.println(Arrays.toString(bytes));
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