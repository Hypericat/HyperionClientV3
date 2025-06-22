package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.HyperionClientV3;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
	@Shadow private @Nullable ButtonWidget exitButton;

	protected GameMenuScreenMixin() {
		super(Text.empty());
	}

	@Inject(at = @At("TAIL"), method = "initWidgets")
	private void onInitWidgets(CallbackInfo ci) {
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Open Last Screen"), button -> this.client.setScreen(HyperionClientV3.lastScreen)).dimensions(this.width / 2 - 67, this.exitButton.getY() + 30, 135, 20).build());
	}

}