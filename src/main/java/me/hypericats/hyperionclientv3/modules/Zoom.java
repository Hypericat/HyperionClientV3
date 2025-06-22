package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.MouseScrollListener;
import me.hypericats.hyperionclientv3.events.eventData.MouseScrollData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class Zoom extends Module implements MouseScrollListener {
    public Zoom() {
        super(false, true, false, false);
    }
    private BooleanOption seizureMode;
    private boolean state = false;
    private final double defaultLevel = 3;
    private Double currentLevel;
    private Double defaultMouseSensitivity;



    //abstract client player entity mixin
    @Override
    public void onEnable() {
    }

    @Override
    protected void initOptions() {
        seizureMode = new BooleanOption(true, "Seizure Mode", false);

        options.addOption(seizureMode);
    }

    public float onGetFov(double fov, MinecraftClient client) {
        SimpleOption<Double> mouseSensitivitySetting =
                client.options.getMouseSensitivity();
        if (!seizureMode.getValue()) {
            mouseSensitivitySetting.setValue(defaultMouseSensitivity * (1.0 / currentLevel));
            return (float) (fov / currentLevel);
        }
        Random random = new Random();
        float rand = random.nextFloat() * 20;
        mouseSensitivitySetting.setValue(defaultMouseSensitivity * (1.0 / rand));
        return (float) (fov / rand);
    }

    @Override
    public void onEvent(EventData data) {
        MouseScrollData mouseScrollData = (MouseScrollData) data;
        double amount = mouseScrollData.getVertical();
        if (amount > 0f)
            currentLevel *= 1.1;
        else if (amount < 0)
            currentLevel *= 0.9;
        currentLevel = MathHelper.clamp(currentLevel, -50, 500);
        data.cancel();
    }
    public void enable(MinecraftClient client) {
        if (this.isEnabled()) return;
        defaultMouseSensitivity = client.options.getMouseSensitivity().getValue();
        currentLevel = defaultLevel;
        EventHandler.register(MouseScrollListener.class, this);
        state = true;
    }
    @Override
    public boolean isEnabled() {
        return state;
    }
    public void disable(MinecraftClient client) {
        if (!isEnabled()) return;
        client.options.getMouseSensitivity().setValue(defaultMouseSensitivity);
        state = false;
        EventHandler.unregister(MouseScrollListener.class, this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public String getName() {
        return "Zoom";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Fov", "Field of View", "Spyglass"};
    }
}
