package me.hypericats.hyperionclientv3;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayerEntity extends OtherClientPlayerEntity {
    public FakePlayerEntity()
    {
        this(MinecraftClient.getInstance().player == null ? -1d : MinecraftClient.getInstance().player.getX(), MinecraftClient.getInstance().player == null ? -1d : MinecraftClient.getInstance().player.getY(), MinecraftClient.getInstance().player == null ? -1d : MinecraftClient.getInstance().player.getZ());
    }
    public FakePlayerEntity(double x, double y, double z)
    {
        super(MinecraftClient.getInstance().world != null ? MinecraftClient.getInstance().world : null, MinecraftClient.getInstance().player != null ? MinecraftClient.getInstance().player.getGameProfile() : null);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        refreshPositionAndAngles(x, y, z, client.player.getYaw(), client.player.getPitch());

        //copyInventory();
        copyPlayerModel(client.player, this);
        //copyRotation();
        //resetCapeMovement();

        spawn();
        discard();
        spawn();
    }

    private void copyInventory()
    {
        getInventory().clone(MinecraftClient.getInstance().player.getInventory());
    }

    private void copyPlayerModel(Entity from, Entity to)
    {
        DataTracker fromTracker = from.getDataTracker();
        DataTracker toTracker = to.getDataTracker();
        Byte playerModel = fromTracker.get(PlayerEntity.PLAYER_MODEL_PARTS);
        toTracker.set(PlayerEntity.PLAYER_MODEL_PARTS, playerModel);
    }

    private void copyRotation()
    {
        headYaw = MinecraftClient.getInstance().player.headYaw;
        bodyYaw = MinecraftClient.getInstance().player.bodyYaw;
    }

    private void resetCapeMovement()
    {
        capeX = getX();
        capeY = getY();
        capeZ = getZ();
    }

    public void spawn()
    {
        MinecraftClient.getInstance().world.addEntity(getId(), this);
    }

    public void despawn()
    {
        discard();
    }

    public void resetPlayerPosition()
    {
        if (MinecraftClient.getInstance().player == null) return;
        MinecraftClient.getInstance().player.refreshPositionAndAngles(getX(), getY(), getZ(), getYaw(),
                getPitch());
    }
}
