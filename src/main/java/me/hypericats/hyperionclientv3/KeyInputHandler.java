package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.KeyInputListener;
import me.hypericats.hyperionclientv3.events.eventData.KeyInputData;
import net.minecraft.client.util.InputUtil;

public class KeyInputHandler implements KeyInputListener {
    public KeyInputHandler() {
        EventHandler.register(KeyInputListener.class, this);
    }


    public void onEvent(EventData data) {
        InputUtil.Key key = ((KeyInputData) data).getKey();
        for (Module m : ModuleHandler.getModulesByKeybind(key)) {
            m.toggle();
        }
    }
}
