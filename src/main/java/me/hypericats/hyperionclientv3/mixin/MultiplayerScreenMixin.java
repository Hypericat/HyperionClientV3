package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.gui.SessionScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
	private ButtonWidget sessionButton;

	protected MultiplayerScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("HEAD"))
	protected void onInit(CallbackInfo ci) {
		this.sessionButton = ButtonWidget.builder(Text.of("Session Login"), button -> {
			MinecraftClient.getInstance().setScreen(new SessionScreen(this));
		}).dimensions(15, this.height - 35, 135, 20).build();

		this.addDrawableChild(sessionButton);
	}
}