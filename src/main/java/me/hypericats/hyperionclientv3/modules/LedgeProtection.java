package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ClientPlayerMoveListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.ClientPlayerMovementData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class LedgeProtection extends Module implements ClientPlayerMoveListener {
    public LedgeProtection() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        ClientPlayerMovementData moveData = (ClientPlayerMovementData) data;
        Vec3d newMovement = adjustMovementForSneaking(moveData.getMovement(), moveData.getMovementType(), client);
        moveData.setNewMovement(newMovement);
    }

    private Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type, MinecraftClient client) {
        if (!client.player.getAbilities().flying && movement.y <= 0.0 && (type == MovementType.SELF || type == MovementType.PLAYER) && this.method_30263(client.player)) {
            double d = movement.x;
            double e = movement.z;
            double f = 0.05;
            while (d != 0.0 && client.player.getWorld().isSpaceEmpty(client.player, client.player.getBoundingBox().offset(d, -client.player.getStepHeight(), 0.0))) {
                if (d < 0.05 && d >= -0.05) {
                    d = 0.0;
                    continue;
                }
                if (d > 0.0) {
                    d -= 0.05;
                    continue;
                }
                d += 0.05;
            }
            while (e != 0.0 && client.player.getWorld().isSpaceEmpty(client.player, client.player.getBoundingBox().offset(0.0, -client.player.getStepHeight(), e))) {
                if (e < 0.05 && e >= -0.05) {
                    e = 0.0;
                    continue;
                }
                if (e > 0.0) {
                    e -= 0.05;
                    continue;
                }
                e += 0.05;
            }
            while (d != 0.0 && e != 0.0 && client.player.getWorld().isSpaceEmpty(client.player, client.player.getBoundingBox().offset(d, -client.player.getStepHeight(), e))) {
                d = d < 0.05 && d >= -0.05 ? 0.0 : (d > 0.0 ? (d -= 0.05) : (d += 0.05));
                if (e < 0.05 && e >= -0.05) {
                    e = 0.0;
                    continue;
                }
                if (e > 0.0) {
                    e -= 0.05;
                    continue;
                }
                e += 0.05;
            }
            movement = new Vec3d(d, movement.y, e);
        }
        return movement;
    }
    private boolean method_30263(ClientPlayerEntity player) {
        return player.isOnGround() || player.fallDistance < player.getStepHeight() && !player.getWorld().isSpaceEmpty(player, player.getBoundingBox().offset(0.0, player.fallDistance - player.getStepHeight(), 0.0));
    }

    @Override
    public void onEnable() {
        EventHandler.register(ClientPlayerMoveListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(ClientPlayerMoveListener.class, this);
    }

    @Override
    public String getName() {
        return "LedgeProtection";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Sneak", "Crouch", "Shift"};
    }
}
