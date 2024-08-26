package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.EntityTargetPriority;
import me.hypericats.hyperionclientv3.enums.EntityTargetType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.List;

public class KillAura extends Module implements TickListener {
    private NumberOption<Double> range;
    private BooleanOption useReachDistance;
    private BooleanOption targetPlayers;
    private BooleanOption targetHostileMobs;
    private BooleanOption targetPassiveMobs;
    private BooleanOption waitCooldown;
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

        //check for cooldown
        if (waitCooldown.getValue() && client.player.getAttackCooldownProgress(0) != 1.0) return;
        
        double range = useReachDistance.getValue() ? client.interactionManager.getReachDistance() : this.range.getValue();

        List<Entity> entityList = PlayerUtils.getEntitiesWithinRange(PlayerUtils.getAttackPlayerPosition(), range, client);
        if (entityList.isEmpty()) return;

        PlayerUtils.parseAttackableEntities(entityList, targetPlayers.getValue(), targetHostileMobs.getValue(), targetPassiveMobs.getValue());
        if (entityList.isEmpty()) return;

        List<Entity> toAttack = PlayerUtils.getAttackListFromEntityTargets(entityList, entityTargetType.getValue(), entityTargetPriority.getValue(), PlayerUtils.getAttackPlayerPosition());

        for (Entity e : toAttack) {
            PacketUtil.attackEntity(e);
        }
        PacketUtil.doFakeHandSwing(Hand.MAIN_HAND);

    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        range = new NumberOption<>(true, "Target Range", 5.0d);
        useReachDistance = new BooleanOption(true, "Use Default Reach Distance", true);
        targetPlayers = new BooleanOption(true, "Target Players", true);
        targetHostileMobs = new BooleanOption(true, "Target Hostile Mobs", true);
        targetPassiveMobs = new BooleanOption(true, "Target Passive Mobs", true);
        waitCooldown = new BooleanOption(true, "Wait Attack Cooldown", true);
        entityTargetType = new EnumStringOption<>(true, "Kill Aura Attack Type", EntityTargetType.SINGLE);
        entityTargetPriority = new EnumStringOption<>(true, "Kill Aura Target Priority", EntityTargetPriority.PLAYER);

        options.addOption(range);
        options.addOption(useReachDistance);
        options.addOption(targetPlayers);
        options.addOption(targetHostileMobs);
        options.addOption(targetPassiveMobs);
        options.addOption(waitCooldown);
        options.addOption(entityTargetType);
        options.addOption(entityTargetPriority);
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
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