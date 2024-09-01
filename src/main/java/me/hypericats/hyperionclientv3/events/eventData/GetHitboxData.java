package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class GetHitboxData extends EventData {
    private Box newBox;
    public Entity getEntity() {
        return getArg(0);
    }
    public Box getOriginalBoundingBox() {
        return getArg(1);
    }
    public void setNewBox(Box box) {
        newBox = box;
    }
    public Box getNewBox() {
        return newBox;
    }
    public GetHitboxData(Entity entity, Box originalBox) {
        super(entity, originalBox);
    }
}
