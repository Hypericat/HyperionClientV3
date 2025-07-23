package me.hypericats.hyperionclientv3.util;

import me.hypericats.hyperionclientv3.FakePlayerEntity;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.enums.EntityTargetPriority;
import me.hypericats.hyperionclientv3.enums.EntityTargetType;
import me.hypericats.hyperionclientv3.modules.Freecam;
import me.hypericats.hyperionclientv3.modules.Friends;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerUtils {
    public static List<Entity> getEntitiesWithinRange(Vec3d pos, double radius, MinecraftClient client) {
        List<Entity> entities = new ArrayList<>();
        if (client.world == null) return entities;

        for (Entity e : client.world.getEntities()) {
            if (e.getId() == client.player.getId() || e instanceof FakePlayerEntity) continue;

            if (pos.squaredDistanceTo(e.getBoundingBox().getCenter()) <= radius * radius) entities.add(e);
        }
        return entities;
    }

    public static List<Entity> parseAttackableEntities(List<Entity> entities, boolean targetPlayers, boolean targetHostile, boolean targetPassive, boolean checkFriends, boolean checkHit) {
        List<Entity> valid = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if ((checkHit && (!e.isAttackable() || !e.isAlive() || e.isInvulnerable() || !e.canHit()))) continue;

            PlayerEntity p = null;
            if (e instanceof PlayerEntity) p = (PlayerEntity) e;
            if (targetPlayers && p != null && !(checkFriends && Friends.isFriend(p))) {
                valid.add(e);
                continue;
            }

            if (targetHostile && e instanceof HostileEntity) {
                valid.add(e);
                continue;
            }

            if (targetPassive && e instanceof PassiveEntity) {
                valid.add(e);
                continue;
            }
        }
        return valid;
    }
    public static int getColorOutline(Entity ent, double range) {
        double distance = MinecraftClient.getInstance().player == null ? 0 : ent.distanceTo(MinecraftClient.getInstance().player);
        double distancePercent = distance / range;
        int green = (int) (255 * distancePercent);
        int red = 255 - green;
        return ColorHelper.getArgb(255, red, green, 0);
    }
    public static List<Entity> getAttackListFromEntityTargets(List<Entity> entities, EntityTargetType targetType, EntityTargetPriority priority, Vec3d pos) {
        List<Entity> targets = new ArrayList<>();
        if (targetType == EntityTargetType.MULTI) {
            targets.addAll(entities);
            return targets;
        }
        switch (priority) {
            case CLOSEST -> {
                targets.add(findEntityInList(entities, Entity.class, true, pos));
            }
            case FURTHEST -> {
                targets.add(findEntityInList(entities, Entity.class, false, pos));
            }
            case PLAYER -> {
                targets.add(findEntityInList(entities, PlayerEntity.class, true, pos));
            }
            case HOSTILE -> {
                targets.add(findEntityInList(entities, HostileEntity.class, true, pos));
            }
            case PASSIVE -> {
                targets.add(findEntityInList(entities, PassiveEntity.class, true, pos));
            }
        }
        return targets;
    }
    public static Entity findEntityInList(List<Entity> entities, Class<?> type, boolean close, Vec3d pos) {
        double bestDistance = close ? Double.MAX_VALUE : Double.MIN_VALUE;
        boolean doOtherTypes = true;
        Entity bestEntity = null;
        for (Entity e : entities) {
            if (type.isInstance(e) && doOtherTypes) {
                doOtherTypes = false;
                bestDistance = close ? Double.MAX_VALUE : Double.MIN_VALUE;
            }
            if (!doOtherTypes && !type.isInstance(e)) continue;
            if (close) {
                if (pos.distanceTo(e.getPos()) < bestDistance) {
                    bestDistance = pos.distanceTo(e.getPos());
                    bestEntity = e;
                }
            } else {
                if (pos.distanceTo(e.getPos()) > bestDistance) {
                    bestDistance = pos.distanceTo(e.getPos());
                    bestEntity = e;
                }
            }
        }
        return bestEntity;
    }
    public static void packetTpToPos(Vec3d pos, MinecraftClient client, boolean updateClient, Vec3d playerPos) {
        packetTpToPos(pos, client, updateClient, playerPos, client.player.isOnGround());
    }
    public static void packetTpToPos(Vec3d pos, MinecraftClient client, boolean updateClient, Vec3d playerPos, boolean onGround) {
        int PacketAmount = (int) Math.ceil(pos.distanceTo(playerPos) / 10) - 1;
        while (PacketAmount > 0) {
            PacketAmount --;
            PacketUtil.sendPosImmediately(client.player.getPos(), onGround);
        }

        PacketUtil.sendPosImmediately(pos, false);

        if (updateClient)
            client.player.setPos(pos.getX(), pos.getY(), pos.getZ());
    }
    public static void packetTpToPos(Vec3d pos, MinecraftClient client, Vec3d playerPos) {
        packetTpToPos(pos, client, true, playerPos);
    }
    public static Vec3d getServerPosition() {
        Freecam freecam = (Freecam) ModuleHandler.getModuleByClass(Freecam.class);
        if (freecam == null || freecam.isDisabled()) return MinecraftClient.getInstance().player.getPos();
        return freecam.getFakePlayerPosition();
    }


    // Stolen from liquid bounce
    public static Vec3d fastAdjustMovementForCollisions(Vec3d movement, Entity player) {
        boolean bl4;

        Box box = new Box(-0.3, 0.0, -0.3, 0.3, 1.8, 0.3).offset(player.getPos());

        List<VoxelShape> entityCollisionList = Collections.emptyList();

        Vec3d vec3d;
        if (movement.lengthSquared() == 0.0) {
            vec3d = movement;
        } else {
            vec3d = Entity.adjustMovementForCollisions(
                    player,
                    movement,
                    box,
                    player.getWorld(),
                    entityCollisionList
            );
        }

        boolean bl = movement.x != vec3d.x;
        boolean bl2 = movement.y != vec3d.y;
        boolean bl3 = movement.z != vec3d.z;

        bl4 = (bl2 && movement.y < 0.0);

        if (player.getStepHeight() > 0.0f && bl4 && (bl || bl3)) {
            Vec3d vec3d2 = Entity.adjustMovementForCollisions(
                    player,
                    new Vec3d(movement.x, player.getStepHeight(), movement.z),
                    box,
                    player.getWorld(),
                    entityCollisionList
            );

            Vec3d vec3d3 = Entity.adjustMovementForCollisions(
                    player,
                    new Vec3d(0.0, (double) player.getStepHeight(), 0.0),
                    box.stretch(movement.x, 0.0, movement.z),
                    player.getWorld(),
                    entityCollisionList
            );

            Vec3d asdf = Entity.adjustMovementForCollisions(
                    player,
                    new Vec3d(movement.x, 0.0, movement.z),
                    box.offset(vec3d3),
                    player.getWorld(),
                    entityCollisionList
            ).add(vec3d3);

            if (vec3d3.y < (double) player.getStepHeight() &&
                    asdf.horizontalLengthSquared() > vec3d2.horizontalLengthSquared()) {
                vec3d2 = asdf;
            }

            if (vec3d2.horizontalLengthSquared() > vec3d.horizontalLengthSquared()) {
                return vec3d2.add(
                        Entity.adjustMovementForCollisions(
                                player,
                                new Vec3d(0.0, -vec3d2.y + movement.y, 0.0),
                                box.offset(vec3d2),
                                player.getWorld(),
                                entityCollisionList
                        )
                );
            }
        }
        return vec3d;
    }
}
