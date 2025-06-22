package me.hypericats.hyperionclientv3.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ViewInventoryScreen extends InventoryScreen {
    PlayerEntity player;

    public ViewInventoryScreen(PlayerEntity player) {
        super(player);
        this.player = player;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
    }


    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {

    }
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = this.x;
        int j = this.y;
        context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, i, j, 0f, 0f, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);

        float yaw = player.getYaw();
        float pitch = player.getPitch();

        // Create quaternions for the rotation
        Quaternionf quaternionYaw = new Quaternionf().rotateY(-yaw * ((float) Math.PI / 180));
        Quaternionf quaternionPitch = new Quaternionf().rotateX(-1 * ((float) Math.PI));
        quaternionYaw = quaternionYaw.mul(quaternionPitch);

        Vector3f vector3f = new Vector3f(0.0F, player.getHeight() / 2.0F + 0.0625F, 0.0F);
        InventoryScreen.drawEntity(context, i + 51, j + 40, 30, vector3f, quaternionYaw, quaternionPitch, player);
    }
}
