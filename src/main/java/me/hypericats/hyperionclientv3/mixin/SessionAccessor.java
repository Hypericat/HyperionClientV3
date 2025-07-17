package me.hypericats.hyperionclientv3.mixin;

import net.minecraft.client.session.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Session.class)
public interface SessionAccessor {
    @Accessor("accessToken")
    String getToken();
}