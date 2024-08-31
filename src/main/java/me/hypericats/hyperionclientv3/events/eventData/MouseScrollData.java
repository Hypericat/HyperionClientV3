package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class MouseScrollData extends EventData {
    public long getWindow() {
        return getArg(0);
    }
    public double getHorizontal() {
        return getArg(1);
    }
    public double getVertical() {
        return getArg(2);
    }
    public MouseScrollData(long window, double horizontal, double vertical) {
        super(window, horizontal, vertical);
    }
}
