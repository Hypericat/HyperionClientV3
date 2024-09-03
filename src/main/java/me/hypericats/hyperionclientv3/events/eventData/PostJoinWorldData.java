package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class PostJoinWorldData extends EventData {
    public ClientWorld getWorld() {
        return getArg(0);
    }

    public PostJoinWorldData(ClientWorld world) {
        super(world);
    }
}
