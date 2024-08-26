package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.FullBrightType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SettingsChangeListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.SettingsChangeData;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;

public class Fullbright extends Module implements SettingsChangeListener, TickListener {
    public Fullbright() {
        super(true);
    }
    private EnumStringOption<FullBrightType> fullBrightType;
    private NumberOption<Double> gammaLevel;
    private int tick;
    @Override
    public void onEvent(EventData data) {
        if (!(data instanceof SettingsChangeData settingsChangeData)) {
            tick ++;
            if (tick > 20) {
                doFullBright();
                tick = 0;
            }
            return;
        }
        if (settingsChangeData.getOption() != fullBrightType) return;
        doFullBright();

    }
    public void doFullBright() {
        switch (fullBrightType.getValue()) {
            case GAMMA -> doGammaLogic();
            case NIGHTVISION -> doNightVisionLogic();
        }
    }
    public void doGammaLogic() {
        disableNightVisionLogic();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        SimpleOption<Double> gamma = client.options.getGamma();
        gamma.setValue(gammaLevel.getValue());
    }
    public void doNightVisionLogic() {
        disableGammaLogic();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, -1));
    }
    public void disableGammaLogic() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        SimpleOption<Double> gamma = client.options.getGamma();
        gamma.setValue(1d);
    }
    public void disableNightVisionLogic() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        client.player.removeStatusEffectInternal(StatusEffects.NIGHT_VISION);
    }
    @Override
    public void onEnable() {
        EventHandler.register(SettingsChangeListener.class, this);
        EventHandler.register(TickListener.class, this);
        doFullBright();
    }

    @Override
    protected void initOptions() {
        fullBrightType = new EnumStringOption<>(true, "FullBright Type", FullBrightType.GAMMA);
        gammaLevel = new NumberOption<>(true, "Gamma Level", 10d);

        this.options.addOption(fullBrightType);
        this.options.addOption(gammaLevel);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(SettingsChangeListener.class, this);
        EventHandler.unregister(TickListener.class, this);
        disableGammaLogic();
        disableNightVisionLogic();
    }

    @Override
    public String getName() {
        return "FullBright";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Night Vision", "Gamma", "Brightness", "Light"};
    }
}
