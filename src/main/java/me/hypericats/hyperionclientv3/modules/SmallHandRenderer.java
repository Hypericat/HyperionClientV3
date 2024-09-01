package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class SmallHandRenderer extends Module {
    public SmallHandRenderer() {
        super(true);
    }
    private static NumberOption<Double> itemScaleX;
    private static NumberOption<Double> itemScaleY;
    private static NumberOption<Double> itemScaleZ;
    private static NumberOption<Double> itemOffsetX;
    private static NumberOption<Double> itemOffsetY;
    private static NumberOption<Double> itemOffsetZ;
    //ItemRendererMixin for code
    @Override
    public void onEnable() {

    }

    @Override
    protected void initOptions() {
        itemScaleX = new NumberOption<>(true, "Matrix Scale X", 0.33d);
        itemScaleY = new NumberOption<>(true, "Matrix Scale Y", 0.33d);
        itemScaleZ = new NumberOption<>(true, "Matrix Scale Z", 0.33d);
        itemOffsetX = new NumberOption<>(true, "Matrix Offset X", 0.0d);
        itemOffsetY = new NumberOption<>(true, "Matrix Offset Y", 0.15d);
        itemOffsetZ = new NumberOption<>(true, "Matrix Offset Z", 0d);

        options.addOption(itemScaleX);
        options.addOption(itemScaleY);
        options.addOption(itemScaleZ);
        options.addOption(itemOffsetX);
        options.addOption(itemOffsetY);
        options.addOption(itemOffsetZ);
    }
    @Override
    public void onDisable() {

    }
    public static Vec3d getOffsetVector() {
        return new Vec3d(itemOffsetX.getValue(), itemOffsetY.getValue(), itemOffsetZ.getValue());
    }
    public static Vec3d getScaleVector() {
        return new Vec3d(itemScaleX.getValue(), itemScaleY.getValue(), itemScaleZ.getValue());
    }

    @Override
    public String getName() {
        return "HandRenderer";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"Small Items", "Tiny"};
    }
}
