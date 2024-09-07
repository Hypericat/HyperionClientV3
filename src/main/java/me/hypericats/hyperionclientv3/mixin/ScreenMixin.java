package me.hypericats.hyperionclientv3.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.PostRenderListener;
import me.hypericats.hyperionclientv3.events.PreRenderEntityListener;
import me.hypericats.hyperionclientv3.events.eventData.PostRenderData;
import me.hypericats.hyperionclientv3.events.eventData.PreRenderEntityData;
import me.hypericats.hyperionclientv3.mixinInterface.IScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin implements IScreen {

	@Shadow @Final private List<Drawable> drawables;

	@Override
	public List<Drawable> getDrawables() {
		return this.drawables;
	}
}