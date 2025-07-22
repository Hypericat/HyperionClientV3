package me.hypericats.hyperionclientv3.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Stolen from meteor
@Mixin(ClientPlayerEntity.class)
public interface ClientPlayerEntityAccessor {

    @Accessor("ticksSinceLastPositionPacketSent")
    void setTickSinceLastMovementPacket(int tickSinceLastMovementPacket);

}
