package me.hypericats.hyperionclientv3.moduleOptions;

public class BooleanOption extends ModuleOption<Boolean>{
    public BooleanOption(boolean shouldSave, String name, Boolean value) {
        super(shouldSave, name, value);
    }
    public void toggle() {
        if (this.getValue()) {
            this.setValue(false);
            return;
        }
        this.setValue(true);
    }
}
