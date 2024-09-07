package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.HyperionClientV3Client;
import net.minecraft.SharedConstants;
import net.minecraft.client.session.Session;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Session.class)
public class SessionMixin {
    @Mutable
    @Shadow @Final private String username;

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/session/Session;username:Ljava/lang/String;", opcode = Opcodes.PUTFIELD))
    private void setUsername(Session instance, String value) {
        if (!HyperionClientV3Client.isDev) {
            this.username = value;
            return;
        }
        this.username = value.startsWith("Player") ? HyperionClientV3Client.defaultName : value;
    }
}