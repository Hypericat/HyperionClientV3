package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

public class InGameOverlayRendererData extends EventData {
    public MatrixStack getMatrixStack() {
        return getArg(1);
    }
    public MinecraftClient getWorld() {
        return getArg(0);
    }
    public InGameOverlayRendererData(MinecraftClient world, MatrixStack matrixStack) {
        super(world, matrixStack);
    }
}
