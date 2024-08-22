package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ModuleToggleListener;
import me.hypericats.hyperionclientv3.events.eventData.ModuleToggleData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.ModuleOptions;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public abstract class Module {
    private boolean state;
    public abstract void onEnable();
    protected ModuleOptions options = new ModuleOptions();
    protected abstract void initOptions();
    private InputUtil.Key keyBind;

    public boolean shouldSaveState() {
        return shouldSaveState.getValue();
    }
    public BooleanOption shouldSaveStateOption() {
        return shouldSaveState;
    }

    private BooleanOption shouldSaveState;
    public abstract void onDisable();
    public Module(boolean shouldSaveState) {
        this.initOptions();
        this.shouldSaveState = new BooleanOption(true, "Save Module State", shouldSaveState);
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
        SoundHandler.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        EventHandler.onEvent(ModuleToggleListener.class, new ModuleToggleData(this));
    }
    public void disable() {
        if (isDisabled()) return;
        state = false;
        ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&cdisabled");
        onDisable();
        SoundHandler.playSound(SoundEvents.BLOCK_GLASS_BREAK);
        EventHandler.onEvent(ModuleToggleListener.class, new ModuleToggleData(this));
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
