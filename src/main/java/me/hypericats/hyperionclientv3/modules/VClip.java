package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.BlockUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class VClip extends Module {
    private NumberOption<Double> distance;
    public VClip() {
        super(false);
    }
    @Override
    public void onEnable() {
        Tp();
        this.disable();
    }
    public void Tp() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.interactionManager == null) return;

        Vec3d pos = client.player.getPos().add(client.player.getRotationVec(client.getTickDelta()).multiply(distance.getValue()));
        PlayerUtils.packetTpToPos(pos, client, client.player.getPos());

        SoundHandler.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT);
    }

    @Override
    protected void initOptions() {
        distance = new NumberOption<>(true, "Clip Distance", 15d);

        options.addOption(distance);
    }
    @Override
    public void onDisable() {

    }
    @Override
    public boolean shouldDisplayChatMessage() {
        return false;
    }
    @Override
    public String getName() {
        return "VClip";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"tp", "Clip", "teleport"};
    }
}
