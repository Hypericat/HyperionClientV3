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
        this.shouldSaveState = new BooleanOption(true, "Save Module State", shouldSaveState);
        this.options.addOption(this.shouldSaveState);
        this.initOptions();
    }
    public Module(boolean shouldSaveState, boolean initOptions) {
        this.shouldSaveState = new BooleanOption(true, "Save Module State", shouldSaveState);
        this.options.addOption(this.shouldSaveState);
        if (initOptions)
            this.initOptions();
    }

    public ModuleOptions getOptions() {
        return this.options;
    }
    public void setEnabled(boolean enabled) {
        setEnabled(enabled, true);
    }
    public void setEnabled(boolean enabled, boolean showChatMessage) {
        if (enabled == this.state) return;
        if (enabled) {
            this.enable(showChatMessage);
            return;
        }
        this.disable(showChatMessage);
    }
    public void toggle() {
        toggle(true);
    }
    public void toggle(boolean showChatMessage) {
        if (isEnabled())  {
            disable(showChatMessage);
            return;
        }
        enable(showChatMessage);
    }
    public void enable() {
        enable(true);
    }
    public void enable(boolean showChatMessage) {
        if (isEnabled()) return;
        state = true;
        if (this.shouldDisplayChatMessage() && showChatMessage) ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&aenabled");
        onEnable();
        if (this.shouldDisplayChatMessage() && showChatMessage) SoundHandler.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        EventHandler.onEvent(ModuleToggleListener.class, new ModuleToggleData(this));
    }
    public void disable() {
        disable(true);
    }
    public void disable(boolean showChatMessage) {
        if (isDisabled()) return;
        state = false;
        if (this.shouldDisplayChatMessage() && showChatMessage) ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&cdisabled");
        onDisable();
        if (this.shouldDisplayChatMessage() && showChatMessage) SoundHandler.playSound(SoundEvents.BLOCK_GLASS_BREAK);
        EventHandler.onEvent(ModuleToggleListener.class, new ModuleToggleData(this));
    }
    public boolean isEnabled() {
        return state;
    }
    public boolean isDisabled() {
        return !state;
    }
    public boolean shouldDisplayChatMessage() {
        return true;
    }
    public abstract String getName();
    public InputUtil.Key getKey() {
        return keyBind;
    }
    public void setKey(InputUtil.Key key, boolean saveKeys) {
        this.keyBind = key;
        if (saveKeys) HyperionClientV3Client.keybindLoader.saveKeys();
    }
    public void setKey(InputUtil.Key key) {
        setKey(key, true);
    }
    public abstract HackType getHackType();
    public String[] getAlias() {
        return new String[0];
    }

}
