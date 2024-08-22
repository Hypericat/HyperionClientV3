package me.hypericats.hyperionclientv3.moduleOptions;


public class EnumStringOption<T extends Enum<?>> extends ModuleOption<T> {
    public EnumStringOption(boolean shouldSave, String name, T value) {
        super(shouldSave, name, value);
    }
    public void setValue(Object value) {
        super.setValue((T) value);
    }
    public void setValue(Object value, boolean callEvent) {
        super.setValue((T) value, callEvent);
    }
}
