package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import net.minecraft.client.gui.DrawContext;

public class ModuleToggleData extends EventData {
    public Module getModule() {
        return getArg(0);
    }
    public ModuleToggleData(Module module) {
        super(module);
    }
}
