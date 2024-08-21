package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.moduleOptions.ModuleOptions;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.util.InputUtil;

public abstract class Module {
    private boolean state;
    public abstract void onEnable();
    protected ModuleOptions options = new ModuleOptions();
    protected abstract void initOptions();
    private InputUtil.Key keyBind;
    public abstract boolean shouldSaveState();
    public abstract void onDisable();
    public Module() {
        this.initOptions();
    }

    public ModuleOptions getOptions() {
        return this.options;
    }

    public void toggle() {
        if (isEnabled())  {
            disable();
            return;
        }
        enable();
    }
    public void enable() {
        if (isEnabled()) return;
        state = true;
        ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&aenabled");
        onEnable();
    }
    public void disable() {
        if (isDisabled()) return;
        state = false;
        ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&cdisabled");
        onDisable();
    }
    public boolean isEnabled() {
        return state;
    }
    public boolean isDisabled() {
        return !state;
    }
    public abstract String getName();
    public InputUtil.Key getKey() {
        return keyBind;
    }
    public void setKey(InputUtil.Key key) {
        this.keyBind = key;
    }
    public abstract HackType getHackType();
    public String[] getAlias() {
        return new String[0];
    }

}
