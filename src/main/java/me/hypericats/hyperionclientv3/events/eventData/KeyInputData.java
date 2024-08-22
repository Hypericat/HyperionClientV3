package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.util.InputUtil;

public class KeyInputData extends EventData {
    public InputUtil.Key getKey() {
        return getArg(0);
    }
    public KeyInputData(InputUtil.Key key) {
        super(key);
    }
}
