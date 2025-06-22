package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.HyperionClientV3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
	protected HandledScreenMixin() {
		super(Text.empty());
	}

	@Inject(at = @At("TAIL"), method = "init")
	private void onInit(CallbackInfo ci) {
		this.addDrawableChild(ButtonWidget.builder(Text.literal("Close Without Packet"), button -> this.client.setScreen(null)).dimensions(this.width / 2 - 67, this.height / 2 - 140, 135, 20).build());

		HyperionClientV3.lastScreen = (HandledScreen<?>) (Object) (this);
	}

}