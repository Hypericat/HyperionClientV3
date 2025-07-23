package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.enums.EntityTargetPriority;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.BlockUtils;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InfAura extends Module implements TickListener {
    private NumberOption<Double> range;
    private BooleanOption randomizePos;
    private BooleanOption targetPlayers;
    private BooleanOption targetHostileMobs;
    private BooleanOption targetPassiveMobs;
    private BooleanOption waitCooldown;

    private BooleanOption blockCrit;
    private EnumStringOption<EntityTargetPriority> entityTargetPriority;
    private FakePlayerEntity fakePlayer;
    private BooleanOption swingHand;

    private PlayerEntity testPlayer;

    public InfAura() {
        super(false);
    }

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.interactionManager == null) return;

        //check for cooldown
        if (waitCooldown.getValue() && client.player.getAttackCooldownProgress(0) != 1.0) return;
        if (ModuleHandler.isModuleEnable(Flight.class) && ((Flight) ModuleHandler.getModuleByClass(Flight.class)).willBypass()) return;

        List<Entity> entityList = PlayerUtils.getEntitiesWithinRange(PlayerUtils.getServerPosition(), this.range.getValue(), client);
        if (entityList.isEmpty()) return;

        entityList = PlayerUtils.parseAttackableEntities(entityList, targetPlayers.getValue(), targetHostileMobs.getValue(), targetPassiveMobs.getValue(), true, true);
        if (entityList.isEmpty()) return;

        List<Entity> toAttack = PlayerUtils.getAttackListFromEntityTargets(entityList, null, entityTargetPriority.getValue(), PlayerUtils.getServerPosition());
        Entity entity = toAttack.get(0);

        Vec3d orginalPos = PlayerUtils.getServerPosition();
        Vec3d tpPos = entity.getBlockPos().toCenterPos();
        if (randomizePos.getValue() || !isSuitablePos(entity.getBlockPos(), client)) tpPos = getPosAround(entity.getPos(), entity, 5.5d, client);
        if (tpPos == null) return;

        client.player.setOnGround(false);
        PlayerUtils.packetTpToPos(tpPos, client, false, orginalPos, false);

        Criticals criticals = (Criticals) ModuleHandler.getModuleByClass(Criticals.class);
        if (criticals != null && criticals.isEnabled() && !blockCrit.getValue()) criticals.crit(tpPos);

        PacketUtil.attackEntityImmediately(entity);
        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = new FakePlayerEntity(tpPos.x, tpPos.y, tpPos.z);

        PlayerUtils.packetTpToPos(orginalPos, client, false, tpPos, false);

        if (swingHand.getValue()) PacketUtil.doFakeHandSwing(Hand.MAIN_HAND);

    }
    public boolean isSuitablePos(BlockPos pos, MinecraftClient client) {
        ClientWorld world = client.world;
        if (world == null || client.player == null) return false;

        BlockPos[] blocks = new BlockPos[2];
        blocks[0] = pos;
        blocks[1] = pos.add(0, -1, 0);
        for (int i = 0; i < blocks.length; i++) {
                BlockPos p = blocks[i];
                BlockState state = world.getBlockState(p);
                if (!state.getCollisionShape(client.world, p).isEmpty()) return false;
                //if (!state.isAir()) return false;
        }

        //Test going and coming back
        return testPosCollision(MinecraftClient.getInstance().player.getPos(), pos.toCenterPos(), client) && testPosCollision(pos.toCenterPos(), MinecraftClient.getInstance().player.getPos(), client);
    }

    public long hash(BlockPos a, BlockPos b) {
        return ((long) a.hashCode() & 0xFFFFFFFFL) | (((long) b.hashCode() & 0xFFFFFFFFL) << 32);
    }

    private boolean testPosCollision(Vec3d start, Vec3d endPos, MinecraftClient client) {
        if (client.player == null || client.player.isSpectator() || client.player.isSleeping()) return false;
        if (client.player.isCreative()) return true;

        testPlayer.setPosition(start); // Also sets bounding box
        testPlayer.setOnGround(false);

        testPlayer.move(MovementType.PLAYER, endPos.subtract(start));

        double dx = endPos.getX() - testPlayer.getX();
        double dy = endPos.getY() - testPlayer.getY();
        if (dy > -0.5 || dy < 0.5) {
            dy = 0.0;
        }
        double dz = endPos.getZ() - testPlayer.getZ();

        double d7 = dx * dx + dy * dy + dz * dz;

        if (d7 > 0.0625D) {
            return false;
        }
        return true;
    }



    public Vec3d getPosAround(Vec3d pos, Entity entity, double maxRange, MinecraftClient client) {
        for (BlockPos block : BlockUtils.getBlockInRange(BlockPos.ofFloored((int) pos.x, (int) pos.y, (int) pos.z), (int) maxRange)) {
            if (block.toBottomCenterPos().add(0d, client.player.isSneaking() ? 1.4175f : 1.62f, 0d).squaredDistanceTo(entity.getBoundingBox().getCenter()) >= maxRange * maxRange) continue;
            if (isSuitablePos(block, client)) return block.toCenterPos();
        }
        return null;
    }

    @Override
    public void onEnable() {
        // Most of this should be called onWorldLoad not here
        EventHandler.register(TickListener.class, this);
        this.testPlayer = new PlayerEntity(MinecraftClient.getInstance().world, new BlockPos(0, 0, 0), MinecraftClient.getInstance().player.headYaw, MinecraftClient.getInstance().getGameProfile()) {
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
    }

    @Override
    protected void initOptions() {
        range = new NumberOption<>(true, "Target Range", 30.0d);
        randomizePos = new BooleanOption(true, "Randomize Teleport Pos", true);
        targetPlayers = new BooleanOption(true, "Target Players", true);
        targetHostileMobs = new BooleanOption(true, "Target Hostile Mobs", true);
        targetPassiveMobs = new BooleanOption(true, "Target Passive Mobs", true);
        waitCooldown = new BooleanOption(true, "Wait Attack Cooldown", true);
        swingHand = new BooleanOption(true, "Swing Hand Packet", true);
        blockCrit = new BooleanOption(true, "Disable Criticals", true);
        entityTargetPriority = new EnumStringOption<>(true, "Kill Aura Target Priority", EntityTargetPriority.PLAYER);

        options.addOption(range);
        options.addOption(randomizePos);
        options.addOption(targetPlayers);
        options.addOption(targetHostileMobs);
        options.addOption(targetPassiveMobs);
        options.addOption(waitCooldown);
        options.addOption(swingHand);
        options.addOption(blockCrit);
        options.addOption(entityTargetPriority);
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "InfAura";
}

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"Infinite Aura", "InfiniteAura", "Reach", "TpAura", "Aura"};
    }

}