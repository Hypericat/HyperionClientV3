package me.hypericats.hyperionclientv3.moduleOptions;

public class NumberOption<T extends Number> extends ModuleOption<T> {
    public NumberOption(boolean shouldSave, String name, T value) {
        super(shouldSave, name, value);
    }
}
