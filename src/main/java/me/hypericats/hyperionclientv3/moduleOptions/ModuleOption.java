package me.hypericats.hyperionclientv3.moduleOptions;


import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SettingsChangeListener;
import me.hypericats.hyperionclientv3.events.eventData.SettingsChangeData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class ModuleOption<T> {
    protected T value;
    protected boolean shouldSave;
    protected String name;
    protected ModuleOption(boolean shouldSave, String name, T value) {
        this.shouldSave = shouldSave;
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return this.name;
    }
    public boolean isShouldSave() {
        return shouldSave;
    }
    public T getValue() {
        return value;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(Object value) {
        setValue(value, true);
    }
    public void setValue(Object value, boolean callEvent) {
        this.value = (T) value;

        if (!callEvent)  return;
        EventHandler.onEvent(SettingsChangeListener.class, new SettingsChangeData(this));
    }
    public void setShouldSave(boolean shouldSave) {
        this.shouldSave = shouldSave;
    }
}
