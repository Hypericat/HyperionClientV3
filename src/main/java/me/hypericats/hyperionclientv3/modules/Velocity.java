package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.VelocitySettingType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

public class Velocity extends Module implements RecievePacketListener {
    public Velocity() {
        super(true);
    }
    private NumberOption<Double> mult;
    private EnumStringOption<VelocitySettingType> velocitySettingType;
    private NumberOption<Double> xMult;
    private NumberOption<Double> yMult;
    private NumberOption<Double> zMult;
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        RecievePacketData packetData = (RecievePacketData) data;
        Packet<?> packet = packetData.getPacket();
        if (packet instanceof EntityVelocityUpdateS2CPacket velocityPacket) {
            Vec3d velocity = new Vec3d(velocityPacket.getVelocityX(), velocityPacket.getVelocityY(), velocityPacket.getVelocityZ());

            if (velocitySettingType.getValue() == VelocitySettingType.INDIVIDUAL) {
                velocity = velocity.multiply(xMult.getValue(), yMult.getValue(), zMult.getValue());
            } else {
                velocity = velocity.multiply(mult.getValue(), mult.getValue(), mult.getValue());
            }

            packetData.setNewPacket(new EntityVelocityUpdateS2CPacket(velocityPacket.getId(), velocity));
        }

    }
    @Override
    public void onEnable() {
        EventHandler.register(RecievePacketListener.class, this);
    }

    @Override
    protected void initOptions() {
        mult = new NumberOption<>(true, "Velocity Multiplier", 0d);
        velocitySettingType = new EnumStringOption<>(true, "Velocity Multiplier Type", VelocitySettingType.MULTIPLIER);
        xMult = new NumberOption<>(true, "X Multiplier", 0d);
        yMult = new NumberOption<>(true, "Y Multiplier", 0d);
        zMult = new NumberOption<>(true, "Z Multiplier", 0d);

        options.addOption(mult);
        options.addOption(velocitySettingType);
        options.addOption(xMult);
        options.addOption(yMult);
        options.addOption(zMult);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(RecievePacketListener.class, this);
    }

    @Override
    public String getName() {
        return "Velocity";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"No KB", "Knockback", "KB"};
    }
}
