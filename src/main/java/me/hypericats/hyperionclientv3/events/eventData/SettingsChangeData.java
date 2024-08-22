package me.hypericats.hyperionclientv3.events.eventData;

import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.moduleOptions.ModuleOption;

public class SettingsChangeData extends EventData {
    public ModuleOption<?> getOption() {
        return getArg(0);
    }
    public SettingsChangeData(ModuleOption<?> option) {
        super(option);
    }
}
