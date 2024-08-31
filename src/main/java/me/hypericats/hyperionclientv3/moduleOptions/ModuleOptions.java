package me.hypericats.hyperionclientv3.moduleOptions;

import java.util.ArrayList;
import java.util.List;

public class ModuleOptions {
    private final List<ModuleOption<?>> options = new ArrayList<>();
    public void addOption(ModuleOption<?> option) {
        options.add(option);
    }
    public ModuleOption<?> getOption(int index) {
        return options.get(index);
    }
    public ModuleOption<?> getOptionByName(String name) {
        for (ModuleOption<?> option : options) {
            if (option == null) return null;
            if (option.getName().equals(name)) return option;
        }
        return null;
    }
    public List<ModuleOption<?>> getAllOptions() {
        return this.options;
    }
}
