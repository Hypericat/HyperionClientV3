package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.FastBreakSafety;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.UpdateBlockBreakingProgressListener;
import me.hypericats.hyperionclientv3.events.eventData.UpdateBlockBreakingProgressData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientPlayerInteractionManager;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.util.PacketUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;

public class FastBreak extends Module implements UpdateBlockBreakingProgressListener {
    public FastBreak() {
        super(true);
    }
    private EnumStringOption<FastBreakSafety> fastBreakSafety;
    private BooleanOption  blockCreative;

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.interactionManager == null) return;
        if (blockCreative.getValue() && !client.interactionManager.getCurrentGameMode().isSurvivalLike()) return;
        ((IClientPlayerInteractionManager) client.interactionManager).setBlockHitDelay(0);
        if (!client.interactionManager.getCurrentGameMode().isSurvivalLike() || fastBreakSafety.getValue() == FastBreakSafety.SAFE) return;

        UpdateBlockBreakingProgressData blockData = (UpdateBlockBreakingProgressData) data;
        if (((IClientPlayerInteractionManager) client.interactionManager).getCurrentBlockBreakingProgress() >= 1f) return;

        PlayerActionC2SPacket.Action action = PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK;
        PacketUtil.send(new PlayerActionC2SPacket(action, blockData.getBlockPos(), blockData.getDirection()));
    }
    @Override
    public void onEnable() {
        EventHandler.register(UpdateBlockBreakingProgressListener.class, this);
    }

    @Override
    protected void initOptions() {
        blockCreative = new BooleanOption(true, "Disable in Creative", false);
        fastBreakSafety = new EnumStringOption<>(true, "Fast Break Safety", FastBreakSafety.OBVIOUS);

        options.addOption(blockCreative);
        options.addOption(fastBreakSafety);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(UpdateBlockBreakingProgressListener.class, this);
    }

    @Override
    public String getName() {
        return "FastBreak";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Fast Mine", "Haste"};
    }
}
