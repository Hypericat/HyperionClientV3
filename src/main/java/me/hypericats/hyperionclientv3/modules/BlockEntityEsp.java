package me.hypericats.hyperionclientv3.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.enums.EspType;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RenderHandListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.events.eventData.RenderHandData;
import me.hypericats.hyperionclientv3.gui.BlockEntityEspEditScreen;
import me.hypericats.hyperionclientv3.gui.CustomWidget;
import me.hypericats.hyperionclientv3.gui.ICustomWidget;
import me.hypericats.hyperionclientv3.gui.WaypointEditScreen;
import me.hypericats.hyperionclientv3.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.chunk.WorldChunk;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlockEntityEsp extends Module implements RenderHandListener, TickListener, ICustomWidget {
    public BlockEntityEsp() {
        super(true);
    }

    private final HashMap<Block, BlockEspTarget> renderMap = new HashMap<>();
    private final List<BlockPos> blockEntities = new ArrayList<>();

    private void remove(Block block) {
        renderMap.remove(block);
    }

    private void add(BlockEspTarget block) {
        renderMap.put(block.getBlockType(), block);
    }

    private void clear() {
        renderMap.clear();
    }

    private boolean exists(Block block) {
        return renderMap.get(block) != null;
    }

    private BlockEspTarget get(Block block) {
        return renderMap.get(block);
    }

    private void updateBlockEntities(MinecraftClient client) {
        blockEntities.clear();
        for (WorldChunk chunk : BlockUtils.getLoadedChunks(client)) {
            blockEntities.addAll(chunk.getBlockEntityPositions());
        }
    }

    private void renderBlockEntities(MatrixStack matrices, float tickDelta, Camera camera, MinecraftClient client) {
        matrices.push();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        for (BlockPos blockPos : blockEntities) {
            Block blockType = client.world.getBlockState(blockPos).getBlock();

            BlockEspTarget blockTarget = get(blockType);
            if (blockTarget == null) continue;
            double dist = blockPos.toCenterPos().distanceTo(client.player.getBoundingBox().getCenter());
            if (dist > blockTarget.getMaxRange()) continue;

            matrices.push();

            Vec3d offset = blockPos.toCenterPos().subtract(camera.getPos());
            matrices.translate(offset.x, offset.y, offset.z);



            //renderLabel(blockType, blockTarget, vertexConsumers, matrices, camera, client);
            if (blockTarget.getEspType() != EspType.TRACER) drawBoxes(matrices, blockTarget);
            if (blockTarget.getEspType() != EspType.BOX)
                drawTracer(blockPos, tickDelta, matrices, camera, blockTarget, client);

            matrices.pop();
        }

        matrices.pop();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawBoxes(MatrixStack matrices, BlockEspTarget target) {
        Box box = new Box(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5);
        matrices.push();

        if (target.isRenderOuterBox()) {
            RenderSystem.setShaderColor(ColorHelper.Argb.getRed(target.getDefaultColor()) / 255f, ColorHelper.Argb.getGreen(target.getDefaultColor()) / 255f, ColorHelper.Argb.getBlue(target.getDefaultColor()) / 255f, target.getOuterBoxAlpha() / 255f);
            RenderUtil.drawOutlinedBox(box, matrices);
        }

        if (target.isRenderOuterBox()) {
            RenderSystem.setShaderColor(ColorHelper.Argb.getRed(target.getDefaultColor()) / 255f, ColorHelper.Argb.getGreen(target.getDefaultColor()) / 255f, ColorHelper.Argb.getBlue(target.getDefaultColor()) / 255f, target.getInnerBoxAlpha() / 255f);
            RenderUtil.drawSolidBox(box, matrices);
        }

        matrices.pop();
    }
    private void renderLabel(Block block, BlockEspTarget target, VertexConsumerProvider vertexConsumerProvider, MatrixStack matrices, Camera camera, MinecraftClient client) {
        //wasn't working so I gave up
        matrices.push();
        String text = block.getName().getString();

        matrices.multiply(camera.getRotation());
        matrices.scale(0.025f, -0.025f, 0.025f);
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        TextRenderer textRenderer = client.textRenderer;
        float h = -textRenderer.getWidth(text) / 2f;
        int light = 0xF000F0;
        textRenderer.draw(text, h, 0f, target.getDefaultColor(), false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.SEE_THROUGH, 0, light);

        matrices.pop();
    }

    private void drawTracer(BlockPos pos, float tickDelta, MatrixStack matrices, Camera camera, BlockEspTarget target, MinecraftClient client) {
        Vec3d tracerStartPos = client.player.getRotationVec(tickDelta).add(camera.getPos()).subtract(pos.toCenterPos());
        matrices.push();

        RenderSystem.setShaderColor(ColorHelper.Argb.getRed(target.getDefaultColor()) / 255f, ColorHelper.Argb.getGreen(target.getDefaultColor()) / 255f, ColorHelper.Argb.getBlue(target.getDefaultColor()) / 255f, target.getTracerAlpha() / 255f);
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        bufferBuilder.vertex(matrix, (float) (tracerStartPos.x), (float) (tracerStartPos.y), (float) (tracerStartPos.z));
        bufferBuilder.vertex(matrix, 0, 0, 0);
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrices.pop();
    }


    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        if (data instanceof RenderHandData renderData) {
            renderBlockEntities(renderData.getMatrices(), renderData.getTickDelta(), client.getBlockEntityRenderDispatcher().camera, client);
            return;
        }
        updateBlockEntities(client);
    }

    @Override
    public void onEnable() {
        EventHandler.register(RenderHandListener.class, this);
        EventHandler.register(TickListener.class, this);
    }

    @Override
    protected void initOptions() {
        addCustomWidgets(new CustomWidget(this, "Open Edit Screen"));
    }

    @Override
    public void onDisable() {
        EventHandler.unregister(RenderHandListener.class, this);
        EventHandler.unregister(TickListener.class, this);
        clear();
    }

    @Override
    public String getName() {
        return "BlockEntityEsp";
    }

    @Override
    public HackType getHackType() {
        return HackType.RENDER;
    }
    @Override
    public void execute(int button) {
        if (button != 0) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) return;
        client.setScreen(new BlockEntityEspEditScreen(HyperionClientV3Client.hyperionClientV3Screen.getModuleEditScreen(), renderMap));
    }

    @Override
    public void playSound(int button) {
        if (button != 0) return;
        SoundHandler.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
    }
}
