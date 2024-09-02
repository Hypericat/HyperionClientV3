package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Jesus extends Module implements TickListener {
    public Jesus() {
        super(true);
    }
    private BooleanOption blockOnShift;
    private NumberOption<Double> swimVelocity;
    private NumberOption<Double> posFromFluid;
    private boolean wasLastTickVelocity;
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.world == null) return;
        if (blockOnShift.getValue() && client.options.sneakKey.isPressed()) return;

        Vec3d currentVelocity = client.player.getVelocity();

        BlockPos currentPos = client.player.getBlockPos();
        BlockState state = client.world.getBlockState(currentPos);
        if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.LAVA) {
            client.player.setVelocity(currentVelocity.getX(), swimVelocity.getValue(), currentVelocity.getZ());
            wasLastTickVelocity = false;
            return;
        }
        Vec3d pos = client.player.getPos();

        BlockPos underPos = BlockPos.ofFloored(pos.subtract(0, this.posFromFluid.getValue(), 0));
        BlockState underState = client.world.getBlockState(underPos);
        if (underState.getBlock() == Blocks.WATER || underState.getBlock() == Blocks.LAVA) {
            client.player.setVelocity(currentVelocity.getX(), 0, currentVelocity.getZ());
            if (!wasLastTickVelocity) client.player.setPos(client.player.getX(), currentPos.getY(), client.player.getZ());
            client.player.setOnGround(true);
            wasLastTickVelocity = true;
            return;
        }
        wasLastTickVelocity = false;
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
        wasLastTickVelocity = false;
    }

    @Override
    protected void initOptions() {
        posFromFluid = new NumberOption<>(true, "Pos Above Fluid", 0.2d);
        swimVelocity = new NumberOption<>(true, "Swim Velocity", 0.3d);
        blockOnShift = new BooleanOption(true, "Cancel on Sneak", true);

        options.addOption(blockOnShift);
        options.addOption(swimVelocity);
        options.addOption(posFromFluid);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "Jesus";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Float", "Walk on Water"};
    }
}
