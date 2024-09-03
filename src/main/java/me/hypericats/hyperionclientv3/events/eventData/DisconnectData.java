package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;

public class DisconnectData extends EventData {
    public Screen getScreen() {
        return getArg(0);
    }

    public DisconnectData(Screen screen) {
        super(screen);
    }
}
