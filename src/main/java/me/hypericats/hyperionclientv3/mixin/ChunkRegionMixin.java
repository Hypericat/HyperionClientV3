package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRegion.class)
public abstract class ChunkRegionMixin {

	@Shadow @Final private static Logger LOGGER;

	@Shadow @Final private ChunkPos lowerCorner;

	@Shadow @Final private ChunkPos upperCorner;

	@Shadow public abstract Chunk getChunk(int chunkX, int chunkZ);

	@Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;[Ljava/lang/Object;)V"), cancellable = true)
	private void injected(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create, CallbackInfoReturnable<Chunk> cir) {
		if (!SharedConstants.isDevelopment) return;
		LOGGER.error("Requested chunk : {} {}", (Object)chunkX, (Object)chunkZ);
		LOGGER.error("Region bounds : {} {} | {} {}", this.lowerCorner.x, this.lowerCorner.z, this.upperCorner.x, this.upperCorner.z);
		if (MinecraftClient.getInstance().player != null) {
			MinecraftClient.getInstance().player.sendMessage(Text.of("Failed to load chunk at " + lowerCorner.x + " " + lowerCorner.z + " | " + upperCorner.x + " " + upperCorner.z));
		}
		cir.setReturnValue(null);
	}
	@Inject(method = "getBlockState", at = @At(value = "HEAD"), cancellable = true)
	private void injected(BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
			if (!SharedConstants.isDevelopment) return;
			Chunk chunk = this.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()));
			if (chunk == null) {
				LOGGER.error("FAILED TO GET BLOCK STATE AT " + pos.toCenterPos().toString());
				if (MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.of("Failed to get block at " + pos.toCenterPos().toString()));
				cir.setReturnValue(Blocks.DIRT.getDefaultState());
				return;
			}
			cir.setReturnValue(chunk.getBlockState(pos));
	}
}