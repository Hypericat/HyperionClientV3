package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public class RenderData extends EventData {
    public MatrixStack getMatrices() {
        return getArg(0);
    }
    public float getTickDelta() {
        return getArg(1);
    }
    public RenderData(MatrixStack matrices, float tickDelta) {
        super(matrices, tickDelta);
    }
}
