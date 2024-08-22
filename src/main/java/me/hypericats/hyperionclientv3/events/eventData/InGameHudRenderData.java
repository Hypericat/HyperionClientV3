package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;

public class InGameHudRenderData extends EventData {
    public DrawContext getDrawContext() {
        return getArg(0);
    }
    public float getTickDelta() {
        return getArg(1);
    }
    public InGameHudRenderData(DrawContext context, float tickDelta) {
        super(context, tickDelta);
    }
}
