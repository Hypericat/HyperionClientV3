package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.EntityTargetPriority;
import me.hypericats.hyperionclientv3.enums.EntityTargetType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class KillAura extends Module implements TickListener, SendPacketListener {
    private NumberOption<Double> range;
    private boolean entityInRange = false;
    private float lastYaw = Float.MIN_VALUE;
    private float lastPitch = Float.MIN_VALUE;
    private BooleanOption useReachDistance;
    private BooleanOption targetPlayers;
    private BooleanOption targetHostileMobs;
    private BooleanOption targetPassiveMobs;
    private BooleanOption waitCooldown;
    private BooleanOption rotation;
    private EnumStringOption<EntityTargetType> entityTargetType;
    private EnumStringOption<EntityTargetPriority> entityTargetPriority;

    public KillAura() {
        super(false);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.interactionManager == null) return;
        if (data instanceof SendPacketData packetData) {
            handlePacket(packetData);
            return;
        }


        double range = useReachDistance.getValue() ? client.player.getEntityInteractionRange() : this.range.getValue();

        //offset playerPosition to getEyeHeight
        List<Entity> entityList = PlayerUtils.getEntitiesWithinRange(PlayerUtils.getServerPosition().add(0, client.player.getStandingEyeHeight(), 0), range, client);
        if (entityList.isEmpty()) {
            entityInRange = false;
            lastYaw = Float.MIN_VALUE;
            lastPitch = Float.MIN_VALUE;
            return;
        }

        PlayerUtils.parseAttackableEntities(entityList, targetPlayers.getValue(), targetHostileMobs.getValue(), targetPassiveMobs.getValue(), true, true);
        if (entityList.isEmpty()) {
            entityInRange = false;
            lastYaw = Float.MIN_VALUE;
            lastPitch = Float.MIN_VALUE;
            return;
        }
        entityInRange = true;
        List<Entity> toAttack = PlayerUtils.getAttackListFromEntityTargets(entityList, entityTargetType.getValue(), entityTargetPriority.getValue(), PlayerUtils.getServerPosition());


        if (rotation.getValue()) doRotation(entityList, client.player.getAttackCooldownProgress(0), client);
        //check for cooldown
        if (waitCooldown.getValue() && client.player.getAttackCooldownProgress(0) != 1.0) return;

        for (Entity e : toAttack) {
            PacketUtil.attackEntity(e);
        }
        PacketUtil.doFakeHandSwing(Hand.MAIN_HAND);

    }
    private void handlePacket(SendPacketData data) {
        if (!rotation.getValue()) return;
        if (!entityInRange) return;
        Packet<?> packet = data.getPacket();
        if (packet instanceof PlayerMoveC2SPacket.LookAndOnGround) data.cancel();
        if (!(packet instanceof PlayerMoveC2SPacket.Full full)) return;
        if (!full.changesPosition()) {
            data.cancel();
            return;
        }
        data.setPacket(new PlayerMoveC2SPacket.PositionAndOnGround(full.getX(1d), full.getY(1d), full.getZ(1d), full.isOnGround()));
    }
    private void doRotation(List<Entity> toAttack, float attackProgress, MinecraftClient client) {
        if (toAttack.size() != 1) return;
        Entity target = toAttack.get(0);
        Vec3d pos = target.getBoundingBox().getCenter();

        float currentYaw = lastYaw == Float.MIN_VALUE ? client.player.getYaw(client.getTickDelta()) : lastYaw;
        float currentPitch = lastPitch == Float.MIN_VALUE ? client.player.getPitch(client.getTickDelta()) : lastPitch;
        double yawRad = Math.toRadians(currentYaw);
        double pitchRad = Math.toRadians(currentPitch);
        Vec3d currentLookDirection = new Vec3d(
                -Math.sin(yawRad) * Math.cos(pitchRad),
                -Math.sin(pitchRad),
                Math.cos(yawRad) * Math.cos(pitchRad)
        );
        Vec3d targetDirection = pos.subtract(client.player.getBoundingBox().getCenter()).normalize();
        Vec3d interpolatedDirection = currentLookDirection.lerp(targetDirection, attackProgress).normalize();
        float newYaw = (float) MathHelper.atan2(interpolatedDirection.z, interpolatedDirection.x) * (180.0f / (float) Math.PI) - 90.0f;
        float newPitch = (float) -MathHelper.atan2(interpolatedDirection.y, Math.sqrt(interpolatedDirection.x * interpolatedDirection.x + interpolatedDirection.z * interpolatedDirection.z)) * (180.0f / (float) Math.PI);
        PlayerMoveC2SPacket.LookAndOnGround packet = new PlayerMoveC2SPacket.LookAndOnGround(newYaw, newPitch, client.player.isOnGround());
        PacketUtil.sendImmediately(packet);
        lastYaw = newYaw;
        lastPitch = newPitch;
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
        EventHandler.register(SendPacketListener.class, this);
    }

    @Override
    protected void initOptions() {
        range = new NumberOption<>(true, "Target Range", 5.0d);
        useReachDistance = new BooleanOption(true, "Use Default Reach Distance", true);
        targetPlayers = new BooleanOption(true, "Target Players", true);
        targetHostileMobs = new BooleanOption(true, "Target Hostile Mobs", true);
        targetPassiveMobs = new BooleanOption(true, "Target Passive Mobs", true);
        waitCooldown = new BooleanOption(true, "Wait Attack Cooldown", true);
        rotation = new BooleanOption(true, "Rotation", true);
        entityTargetType = new EnumStringOption<>(true, "Kill Aura Attack Type", EntityTargetType.SINGLE);
        entityTargetPriority = new EnumStringOption<>(true, "Kill Aura Target Priority", EntityTargetPriority.PLAYER);

        options.addOption(range);
        options.addOption(useReachDistance);
        options.addOption(targetPlayers);
        options.addOption(targetHostileMobs);
        options.addOption(targetPassiveMobs);
        options.addOption(waitCooldown);
        options.addOption(rotation);
        options.addOption(entityTargetType);
        options.addOption(entityTargetPriority);
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(SendPacketListener.class, this);
    }

    @Override
    public String getName() {
        return "KillAura";
    }

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"AimBot", "Kill Aura"};
    }

}