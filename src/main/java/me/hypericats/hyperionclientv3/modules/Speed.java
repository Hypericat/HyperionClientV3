package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.SpeedType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.moduleOptions.SliderOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Speed extends Module implements TickListener {
    public Speed() {
        super(true);
    }
    private EnumStringOption<SpeedType> speedType;
    private SliderOption<Double> velocitySpeed;
    private NumberOption<Double> velocityBoostMultiplier;
    private NumberOption<Double> bHopJumpMultiplier;
    private NumberOption<Double> bHopSpeedMultiplier;
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        switch (speedType.getValue()) {
            case CONTROL -> doControl(client);
            case BHOP -> doBHop(client);
            case JUMP -> doJumps(client);
            case VELOCITY -> doVelocity(client, client.options.sprintKey.isPressed() ? velocitySpeed.getValue() / 5d * velocityBoostMultiplier.getValue() : velocitySpeed.getValue() / 5d);
        }
    }
    private void doVelocity(MinecraftClient client, double speed) {
        if (client.player.hasVehicle()) return;

        Vec3d velocity = getHorizontalVelocity(client, speed);
        client.player.setVelocity(velocity.getX(), client.player.getVelocity().getY(), velocity.getZ());
    }

    private Vec3d getHorizontalVelocity(MinecraftClient client, double speed) {
        Vec3d velocity = Vec3d.ZERO;

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

    private void doJumps(MinecraftClient client) {

    }
    private void doBHop(MinecraftClient client) {
        Vec3d newVelocity = client.player.getVelocity();
        if (client.player.isOnGround()) {
            newVelocity = new Vec3d(newVelocity.getX(),  0.42f * bHopJumpMultiplier.getValue().floatValue(), newVelocity.getZ());
        }
        Vec3d facing = client.player.getRotationVecClient();
        facing = new Vec3d(facing.getX(), 0, facing.getZ()).normalize();
        newVelocity = new Vec3d(facing.getX() * bHopSpeedMultiplier.getValue() / 2, newVelocity.getY(), facing.getZ() * bHopSpeedMultiplier.getValue() / 2);
        client.player.setVelocity(newVelocity);
    }
    private void doControl(MinecraftClient client) {
        //client.player.sendMessage(Text.of(client.player.getVelocity().toString()));
        doVelocity(client, client.options.sprintKey.isPressed() ? 0.16f : 0.12f);
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        speedType = new EnumStringOption<>(true, "Speed Type", SpeedType.VELOCITY);
        velocitySpeed = new SliderOption<>(true, "Velocity Speed", 1d, 10d, 0.25d, 0.25d);
        velocityBoostMultiplier = new NumberOption<>(true, "Velocity Sprint Multiplier", 2.5d);
        bHopSpeedMultiplier = new NumberOption<>(true, "BHop Speed Multiplier", 1d);
        bHopJumpMultiplier = new NumberOption<>(true, "BHop Jump Multiplier", 1.5d);

        options.addOption(speedType);
        options.addOption(velocitySpeed);
        options.addOption(velocityBoostMultiplier);
        options.addOption(bHopSpeedMultiplier);
        options.addOption(bHopJumpMultiplier);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Bhop", "Fast", "Run", "Sprint", "Air Control", "Air movement"};
    }
}
