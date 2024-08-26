package me.hypericats.hyperionclientv3.mixinInterface;

import net.minecraft.client.option.KeyBinding;

public interface IKeyBinding {
    public boolean isPressed(KeyBinding key);
    public void resetPressedState();
    public boolean isActuallyPressed();
}
