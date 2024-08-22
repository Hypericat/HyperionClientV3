package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.KeyInputListener;
import me.hypericats.hyperionclientv3.events.eventData.KeyInputData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
	@Shadow
	private boolean pressed;
	private InputUtil.Key boundKey;

	@Inject(at = @At("HEAD"), method = {"onKeyPressed"})
	private static void onKeyPressed(InputUtil.Key key, CallbackInfo ci) {
		EventHandler.onEvent(KeyInputListener.class, new KeyInputData(key));
	}
	//@Override
	public boolean isActuallyPressed()
	{
		long handle = MinecraftClient.getInstance().getWindow().getHandle();
		int code = boundKey.getCode();
		return InputUtil.isKeyPressed(handle, code);
		//return false;
	}
	public boolean isPressed(KeyBinding key)
	{
		int code = key.getDefaultKey().getCode();
		long handle = MinecraftClient.getInstance().getWindow().getHandle();
		//int code = key.getCode();
		return InputUtil.isKeyPressed(handle, code);
		//return false;
	}

	//@Override
	public void resetPressedState()
	{
		setPressed(isActuallyPressed());
	}
	//public void pressKey(KeyBinding key) {
	//    key.setPressed(true);
	//}

	@Shadow
	public abstract void setPressed(boolean pressed);
}