package me.hypericats.hyperionclientv3.moduleOptions;

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
    public void setValue(T value) {
        this.value = value;
    }
    public void setShouldSave(boolean shouldSave) {
        this.shouldSave = shouldSave;
    }
}
