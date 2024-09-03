package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.world.ClientWorld;

public class PreJoinWorldData extends EventData {
    public ClientWorld getWorld() {
        return getArg(0);
    }

    public PreJoinWorldData(ClientWorld world) {
        super(world);
    }
}
