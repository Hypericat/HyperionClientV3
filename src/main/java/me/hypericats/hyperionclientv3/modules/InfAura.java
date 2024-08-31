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
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        List<Entity> entityList = PlayerUtils.getEntitiesWithinRange(PlayerUtils.getServerPosition(), this.range.getValue(), client);
        if (entityList.isEmpty()) return;

        PlayerUtils.parseAttackableEntities(entityList, targetPlayers.getValue(), targetHostileMobs.getValue(), targetPassiveMobs.getValue(), true, true);
        if (entityList.isEmpty()) return;

        List<Entity> toAttack = PlayerUtils.getAttackListFromEntityTargets(entityList, null, entityTargetPriority.getValue(), PlayerUtils.getServerPosition());
        Entity entity = toAttack.get(0);

        Vec3d orginalPos = PlayerUtils.getServerPosition();
        Vec3d tpPos = entity.getPos();
        if (!isSuitablePos(BlockPos.ofFloored(tpPos), client) || randomizePos.getValue()) tpPos = getPosAround(entity.getPos(), entity, 3, client);
        if (tpPos == null) return;
        PlayerUtils.packetTpToPos(tpPos, client, false, orginalPos);

        Criticals criticals = (Criticals) ModuleHandler.getModuleByClass(Criticals.class);
        if (criticals != null && criticals.isEnabled() && !blockCrit.getValue()) criticals.crit(tpPos);

        PacketUtil.attackEntityImmediately(entity);
        if (fakePlayer != null) fakePlayer.despawn();
        fakePlayer = new FakePlayerEntity(tpPos.x, tpPos.y, tpPos.z);

        PlayerUtils.packetTpToPos(orginalPos, client, false, tpPos);



        if (swingHand.getValue()) PacketUtil.doFakeHandSwing(Hand.MAIN_HAND);

    }
    public boolean isSuitablePos(BlockPos pos, MinecraftClient client) {
        ClientWorld world = client.world;
        if (world == null) return false;
        BlockPos[] blocks = new BlockPos[2];
        blocks[0] = pos;
        blocks[1] = pos.add(0, -1, 0);
        for (int i = 0; i < blocks.length; i++) {
                BlockPos p = blocks[i];
                BlockState state = world.getBlockState(p);
                if (!state.isAir()) return false;
        }
        return true;
    }
    public Vec3d getPosAround(Vec3d pos, Entity entity, double maxRange, MinecraftClient client) {
        List<BlockPos> possibleBlockPos = new ArrayList<>();
        for (BlockPos block : BlockUtils.getBlockInRange(BlockPos.ofFloored((int) pos.x, (int) pos.y, (int) pos.z), (int) maxRange)) {
            if (block.toCenterPos().add(0, 0, 0).squaredDistanceTo(entity.getPos()) >= 8.0) continue;
            if (isSuitablePos(block, client)) possibleBlockPos.add(block);
        }
        if (possibleBlockPos.isEmpty()) return null;
        Random random = new Random();
        return possibleBlockPos.get(random.nextInt(possibleBlockPos.size())).toCenterPos().subtract(0, 1, 0);
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
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