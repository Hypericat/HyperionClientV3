package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ModuleToggleListener;
import me.hypericats.hyperionclientv3.events.eventData.ModuleToggleData;
import me.hypericats.hyperionclientv3.gui.CustomWidget;
import me.hypericats.hyperionclientv3.gui.ICustomWidget;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.ModuleOptions;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {
    private boolean state;
    public abstract void onEnable();
    protected ModuleOptions options = new ModuleOptions();
    protected List<CustomWidget> customWidgets = new ArrayList<>();
    protected abstract void initOptions();
    private InputUtil.Key keyBind;
    private boolean sendMessage;
    public boolean shouldSaveState() {
        return shouldSaveState.getValue();
    }
    public BooleanOption shouldSaveStateOption() {
        return shouldSaveState;
    }

    private BooleanOption shouldSaveState;
    private boolean shouldToggle;
    public abstract void onDisable();
    public Module(boolean shouldSaveState) {
        this(shouldSaveState, true);
    }
    public Module(boolean shouldSaveState, boolean initOptions) {
        this(shouldSaveState, initOptions, true);
    }
    public Module(boolean shouldSaveState, boolean initOptions, boolean sendMessage) {
        this(shouldSaveState, initOptions, sendMessage, true);
    }
    public Module(boolean shouldSaveState, boolean initOptions, boolean sendMessage, boolean shouldToggle) {
        this.shouldSaveState = new BooleanOption(true, "Save Module State", shouldSaveState);
        this.options.addOption(this.shouldSaveState);
        this.sendMessage = sendMessage;
        this.shouldToggle = shouldToggle;

        if (initOptions)
            this.initOptions();
    }

    public ModuleOptions getOptions() {
        return this.options;
    }
    public void addCustomWidgets(CustomWidget iCustomWidget) {
        this.customWidgets.add(iCustomWidget);
    }
    public List<CustomWidget> getCustomWidgets() {
        return this.customWidgets;
    }
    public void setEnabled(boolean enabled) {
        setEnabled(enabled, sendMessage);
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
        toggle(sendMessage);
    }
    public void toggle(boolean showChatMessage) {
        if (!shouldToggle) return;
        if (isEnabled())  {
            disable(showChatMessage);
            return;
        }
        enable(showChatMessage);
    }
    public void enable() {
        enable(sendMessage);
    }
    public void enable(boolean showChatMessage) {
        if (!shouldToggle) return;
        if (isEnabled()) return;
        state = true;
        if (this.shouldDisplayChatMessage() && showChatMessage) ChatUtils.sendOfficial("&&4" + getName() + "&&r has been &&aenabled");
        onEnable();
        if (this.shouldDisplayChatMessage() && showChatMessage) SoundHandler.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
        EventHandler.onEvent(ModuleToggleListener.class, new ModuleToggleData(this));
    }
    public void disable() {
        disable(sendMessage);
    }
    public void disable(boolean showChatMessage) {
        if (!shouldToggle) return;
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
        return sendMessage;
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
