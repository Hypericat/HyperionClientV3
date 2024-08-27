package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;

public class ClientPlayerMovementData extends EventData {
    private Vec3d newMovement;
    public void setNewMovement(Vec3d newMovement) {
        this.newMovement = newMovement;
    }
    public Vec3d getNewMovement() {
        return newMovement;
    }
    public MovementType getMovementType() {
        return getArg(0);
    }
    public Vec3d getMovement() {
        return getArg(1);
    }
    public ClientPlayerMovementData(MovementType movementType, Vec3d movement) {
        super(movementType, movement);
    }
}
