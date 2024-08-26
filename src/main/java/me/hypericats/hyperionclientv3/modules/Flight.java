package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.moduleOptions.SliderOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Flight extends Module implements TickListener {
    private SliderOption<Double> dropDistance;
    private SliderOption<Double> speedModifier;
    private NumberOption<Double> sprintModifier;
    private NumberOption<Integer> tickInterval;
    private BooleanOption doFlyBypass;
    private int ticks = 0;

    public Flight() {
        super(false);
    }

    @Override
    public void onEvent(EventData data) {
        Freecam freecam = (Freecam) ModuleHandler.getModuleByClass(Freecam.class);
        if (freecam != null && freecam.isEnabled()) return;
        fly();
    }
    public void fly() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.player.hasVehicle()) return;
        ticks++;

        double speed = speedModifier.getValue();
        if (client.options.sprintKey.isPressed()) speed *= sprintModifier.getValue();

        Vec3d velocity = getFlyVelocity(client, speed);
        client.player.setVelocity(velocity);

        if (!doFlyBypass.getValue()) return;
        if (ticks >= tickInterval.getValue()) {
            PacketUtil.sendPos(client.player.getX(), client.player.getY() - dropDistance.getValue(), client.player.getZ(), client.player.isOnGround());
            ticks = 0;
        }
        if (ticks == 1) {
            PacketUtil.sendPos(client.player.getX(), client.player.getY() + dropDistance.getValue(), client.player.getZ(), client.player.isOnGround());
        }
    }

    private Vec3d getFlyVelocity(MinecraftClient client, double speed) {
        Vec3d velocity = Vec3d.ZERO;
        if (client.options.jumpKey.isPressed()) velocity = velocity.add(0.0, speed / 2.0, 0.0);

        if (client.options.sneakKey.isPressed()) velocity = velocity.add(0.0, speed / 2.0 * -1.0, 0.0);

        float yawRadians = (float) Math.toRadians(client.player.getYaw());

        //forwards
        if (client.options.forwardKey.isPressed())
            velocity = velocity.add(MathHelper.sin(-yawRadians) * speed, 0, MathHelper.cos(-yawRadians) * speed);

        //backwards
        if (client.options.backKey.isPressed())
            velocity = velocity.add(MathHelper.sin(yawRadians) * speed, 0, MathHelper.cos(yawRadians) * speed * -1);

        //leftwards
        if (client.options.leftKey.isPressed())
            velocity = velocity.add(MathHelper.sin(-(yawRadians - MathHelper.PI / 2)) * speed, 0, MathHelper.cos(-(yawRadians - MathHelper.PI / 2)) * speed);

        //rightwards
        if (MinecraftClient.getInstance().options.rightKey.isPressed())
            velocity = velocity.add(MathHelper.sin(-(yawRadians + MathHelper.PI / 2)) * speed, 0, MathHelper.cos(-(yawRadians + MathHelper.PI / 2)) * speed);
        return velocity;
    }

    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        dropDistance = new SliderOption<>(true, "Bypass Drop Distance", 0.09d, 0.5d, 0.01d, 0.01d);
        speedModifier = new SliderOption<>(true, "Base Fly Speed", 1d, 10d, 0.25d, 0.25d);
        sprintModifier = new NumberOption<>(true, "Sprint Fly Multiplier", 4d);
        tickInterval = new NumberOption<>(true, "Fly Bypass Interval", 39);
        doFlyBypass = new BooleanOption(true, "Do Fly Bypass", true);

        options.addOption(dropDistance);
        options.addOption(speedModifier);
        options.addOption(sprintModifier);
        options.addOption(tickInterval);
        options.addOption(doFlyBypass);
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "Flight";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Fly", "FlyHack"};
    }
}
