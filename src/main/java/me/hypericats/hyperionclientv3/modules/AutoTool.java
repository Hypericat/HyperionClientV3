package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.UpdateBlockBreakingProgressListener;
import me.hypericats.hyperionclientv3.events.eventData.UpdateBlockBreakingProgressData;
import me.hypericats.hyperionclientv3.util.IAbstractBlock;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AutoTool extends Module implements TickListener, UpdateBlockBreakingProgressListener {
    public AutoTool() {
        super(true);
    }
    private int oldSlot = -1;
    private int tickCounter = 0;
    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.interactionManager == null) return;
        if (client.world == null) return;
        if (client.interactionManager.getCurrentGameMode() != GameMode.SURVIVAL) return;


        if (data instanceof UpdateBlockBreakingProgressData blockData) {
            onAttackBlock(blockData.getBlockPos(), blockData.getDirection(), client);
            return;
        }

        updateSlot(client.interactionManager, client);
    }
    public void updateSlot(ClientPlayerInteractionManager interactionManager, MinecraftClient client) {
        if (oldSlot == -1) return;
        if (interactionManager.isBreakingBlock()) tickCounter = 0;
        if (!interactionManager.isBreakingBlock()) {
            tickCounter ++;
        }
        if (tickCounter < 5) return;

        client.player.getInventory().selectedSlot = oldSlot;
        oldSlot = -1;
    }
    public void onAttackBlock(BlockPos block, Direction dir, MinecraftClient client) {
        syncSlot(block, client);
        //ChatUtils.sendDebug(1f / client.world.getBlockState(block).calcBlockBreakingDelta(client.player, client.world, block));
    }
    public void syncSlot(BlockPos block, MinecraftClient client) {
        BlockState state = client.world.getBlockState(block);
        if (state.getOutlineShape(client.world, block) == VoxelShapes.empty()) return;

        if (oldSlot == -1) oldSlot = client.player.getInventory().selectedSlot;
        int bestSlot = getBestSlot(client, state);
        if (bestSlot < 0) {
            oldSlot = -1;
            return;
        }
        client.player.getInventory().selectedSlot = bestSlot;
    }
    private static int getBestSlot(MinecraftClient client, BlockState state) {
        int slot = -1;
        float bestBreakTime = 0f;
        for (int s = 0; s < 9; s++) {
            ItemStack item = client.player.getInventory().getStack(s);
            if (getSpeed(item, state) > bestBreakTime && item.getMaxDamage() - item.getDamage() > 1) {
                bestBreakTime = getSpeed(item, state);
                slot = s;
            }
            if ((!item.isDamageable()) && getSpeed(item, state) >= bestBreakTime) {
                bestBreakTime = getSpeed(item, state);
                slot = s;
            }
        }
        return slot;
    }
    private static float getSpeed(ItemStack stack, BlockState state) {
        float speed = stack.getMiningSpeedMultiplier(state);

        if(speed <= 1) return speed;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return speed;
        Registry<Enchantment> enchantmentRegistry = client.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        Optional<RegistryEntry.Reference<Enchantment>> eff = enchantmentRegistry.getEntry(Enchantments.EFFICIENCY.getValue());
        if (eff.isEmpty()) return speed;

        int efficiency = EnchantmentHelper.getLevel(eff.get(), stack);
        if(efficiency > 0 && !stack.isEmpty())
            speed += efficiency * efficiency + 1;
        return speed;
    }

    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
        EventHandler.register(UpdateBlockBreakingProgressListener.class, this);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
        EventHandler.unregister(UpdateBlockBreakingProgressListener.class, this);
    }

    @Override
    public String getName() {
        return "AutoTool";
    }

    @Override
    public HackType getHackType() {
        return HackType.UTIL;
    }

    public String[] getAlias() {
        return new String[] {"Hotbar", "Tool"};
    }
}
