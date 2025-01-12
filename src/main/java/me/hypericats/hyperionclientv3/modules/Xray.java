package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderBlockSideListener;
import me.hypericats.hyperionclientv3.events.eventData.RenderBlockSideData;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.HashSet;

public class Xray extends Module implements RenderBlockSideListener {
    //AbstractBlockStateMixin for light level changes

    HashSet<Block> renderedBlocks;

    public Xray() {
        super(true);
    }

    @Override
    public void onEvent(EventData data) {
        if (renderedBlocks.contains(((RenderBlockSideData) data).getBlockState().getBlock())) {
            data.setCancelled(false);
            return;
        }
        data.cancel();
    }
    @Override
    public void onEnable() {
        EventHandler.register(RenderBlockSideListener.class, this);
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    @Override
    protected void initOptions() {
        renderedBlocks = new HashSet<>();

        renderedBlocks.add(Blocks.DIAMOND_ORE);
        renderedBlocks.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        renderedBlocks.add(Blocks.GOLD_ORE);
        renderedBlocks.add(Blocks.DEEPSLATE_GOLD_ORE);
        renderedBlocks.add(Blocks.NETHERITE_BLOCK);
        renderedBlocks.add(Blocks.ANCIENT_DEBRIS);
        renderedBlocks.add(Blocks.IRON_ORE);
        renderedBlocks.add(Blocks.DEEPSLATE_IRON_ORE);
        renderedBlocks.add(Blocks.EMERALD_BLOCK);
        renderedBlocks.add(Blocks.DEEPSLATE_EMERALD_ORE);
        renderedBlocks.add(Blocks.CHEST);
        renderedBlocks.add(Blocks.ENDER_CHEST);
        renderedBlocks.add(Blocks.DIAMOND_BLOCK);
        renderedBlocks.add(Blocks.FURNACE);
        renderedBlocks.add(Blocks.SHULKER_BOX); // Other colors too
        renderedBlocks.add(Blocks.OBSIDIAN);

    }

    @Override
    public void onDisable() {
        EventHandler.unregister(RenderBlockSideListener.class, this);
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    @Override
    public String getName() {
        return "Xray";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }

    public String[] getAlias() {
        return new String[] {"X-ray", "block esp"};
    }
}
