package me.hypericats.hyperionclientv3.modules;

import com.mojang.blaze3d.systems.RenderSystem;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.EspColorType;
import me.hypericats.hyperionclientv3.enums.EspType;
import me.hypericats.hyperionclientv3.events.RenderListener;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.moduleOptions.SliderOption;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class Esp extends Module implements RenderListener, TickListener {
    public Esp(boolean shouldSaveState, boolean doFriends) {
        super(shouldSaveState, false);
        this.doFriends = doFriends;
        initOptions();
    }
    private final boolean doFriends;
    private SliderOption<Double> range;
    private EnumStringOption<EspType> espType;
    private EnumStringOption<EspColorType> espColorType;
    private SliderOption<Double> extraBoxSize;
    private SliderOption<Double> innerBoxAlpha;
    private SliderOption<Double> outerBoxAlpha;
    private SliderOption<Double> tracerAlpha;
    private BooleanOption renderOuterBox;
    private BooleanOption renderInnerBox;
    private NumberOption<Integer> defaultColor;
    private NumberOption<Integer> friendColor;
    private final List<Entity> toRender = new ArrayList<>();
    protected abstract void initTargets(MinecraftClient client);
    public void clearTargetStack() {
        toRender.clear();
    }
    public void addToTargetStack(Entity entity) {
        toRender.add(entity);
    }
    public List<Entity> getTargetStack() {
        return toRender;
    }
    public double getRange() {
        return range.getValue();
    }
    protected void render(List<Entity> targets, MatrixStack matrices, float tickDelta, MinecraftClient client) {

        Camera camera = client.getEntityRenderDispatcher().camera;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        matrices.push();

        Vec3d cameraPos = camera.getPos();
        Vec3d roundCamPos = new Vec3d(((int) cameraPos.getX()) >> 9 << 9, 0, ((int) cameraPos.getZ()) >> 9 << 9);
        Vec3d offset = roundCamPos.subtract(cameraPos);
        matrices.translate(offset.x, offset.y, offset.z);

        if (espType.getValue() == EspType.BOX || espType.getValue() == EspType.BOXANDTRACER) drawBoxes(matrices, tickDelta, roundCamPos, targets);
        if (espType.getValue() == EspType.TRACER || espType.getValue() == EspType.BOXANDTRACER) drawTracer(matrices, tickDelta, roundCamPos, client, camera, targets);

        matrices.pop();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawTracer(MatrixStack matrices, float tickDelta, Vec3d roundCamPos, MinecraftClient client, Camera camera, List<Entity> targets) {
        Vec3d tracerStartPos = client.player.getRotationVec(tickDelta).add(camera.getPos()).subtract(roundCamPos);
        for (Entity e : targets) {
            matrices.push();

            Vec3d relativeEntityPos = e.getBoundingBox().offset(e.getLerpedPos(tickDelta).subtract(e.getPos())).getCenter().subtract(roundCamPos);
            matrices.translate(relativeEntityPos.getX(), relativeEntityPos.getY(), relativeEntityPos.getZ());

            int color = Friends.isFriend(e) ? friendColor.getValue() : (espColorType.getValue() == EspColorType.STATIC ? defaultColor.getValue() : PlayerUtils.getColorOutline(e, range.getValue()));
            float red = ColorHelper.Argb.getRed(color) / 255f;
            float green = ColorHelper.Argb.getGreen(color) / 255f;
            float blue = ColorHelper.Argb.getBlue(color) / 255f;
            RenderSystem.setShaderColor(red, green, blue, tracerAlpha.getValue().floatValue() / 255f);

            Matrix4f matrix = matrices.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            RenderSystem.setShader(GameRenderer::getPositionProgram);
            bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);

            bufferBuilder.vertex(matrix, (float) (tracerStartPos.x - relativeEntityPos.x), (float) (tracerStartPos.y - relativeEntityPos.y), (float) (tracerStartPos.z - relativeEntityPos.z)).next();
            bufferBuilder.vertex(matrix, 0, 0, 0).next();

            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            matrices.pop();
        }
    }

    private void drawBoxes(MatrixStack matrices, float tickDelta, Vec3d roundCamPos, List<Entity> targets) {
        for (Entity e : targets) {
            matrices.push();
            Vec3d pos = e.getLerpedPos(tickDelta).subtract(roundCamPos);
            matrices.translate(pos.x, pos.y, pos.z);


            int color = Friends.isFriend(e) ? friendColor.getValue() : espColorType.getValue() == EspColorType.STATIC ? defaultColor.getValue() : PlayerUtils.getColorOutline(e, range.getValue());
            float red = ColorHelper.Argb.getRed(color) / 255f;
            float green = ColorHelper.Argb.getGreen(color) / 255f;
            float blue = ColorHelper.Argb.getBlue(color) / 255f;

            RenderSystem.setShaderColor(red, green, blue, outerBoxAlpha.getValue().floatValue() / 255f);
            Box box = new Box(-0.5, 0, -0.5, 0.5, 1, 0.5);

            if (renderOuterBox.getValue()) {
                matrices.push();
                matrices.scale((float) (e.getWidth() + extraBoxSize.getValue()), (float) (e.getHeight() + extraBoxSize.getValue()), (float) (e.getWidth() + extraBoxSize.getValue()));
                RenderUtil.drawOutlinedBox(box, matrices);
                matrices.pop();
            }
            if (renderInnerBox.getValue()) {
                RenderSystem.setShaderColor(red, green, blue, (float) (innerBoxAlpha.getValue() / 255f));
                matrices.push();
                matrices.scale(e.getWidth(), e.getHeight(), e.getWidth());
                RenderUtil.drawSolidBox(box, matrices);
                matrices.pop();
            }

            matrices.pop();
        }
    }

    @Override
    protected void initOptions() {
        range = new SliderOption<>(true, "Range", 100d, 300d, 10d, 2.5d);
        espType = new EnumStringOption<>(true, "ESP Type", EspType.BOXANDTRACER);
        espColorType = new EnumStringOption<>(true, "ESP Color Type", EspColorType.NEAR);
        defaultColor = new NumberOption<>(true, "Default Color", ColorHelper.Argb.getArgb(255, 255, 0, 0));
        friendColor = new NumberOption<>(true, "Friend Color", ColorHelper.Argb.getArgb(255, 0, 0, 255));
        innerBoxAlpha = new SliderOption<>(true, "Inner Box Alpha", 100d, 255d, 0d, 5d);
        outerBoxAlpha = new SliderOption<>(true, "Outer Box Alpha", 255d, 255d, 0d, 5d);
        tracerAlpha = new SliderOption<>(true, "Tracer Alpha", 255d, 255d, 0d, 5d);
        renderInnerBox = new BooleanOption(true, "Render Inner Box", true);
        renderOuterBox = new BooleanOption(true, "Render Outer Box", true);
        extraBoxSize = new SliderOption<>(true, "Extra Box Size", 0.1d, 3d, 0d, 0.1d);

        options.addOption(range);
        options.addOption(espType);
        options.addOption(espColorType);
        options.addOption(defaultColor);
        if (doFriends)
            options.addOption(friendColor);
        options.addOption(innerBoxAlpha);
        options.addOption(outerBoxAlpha);
        options.addOption(tracerAlpha);
        options.addOption(renderInnerBox);
        options.addOption(renderOuterBox);
        options.addOption(extraBoxSize);
    }
}