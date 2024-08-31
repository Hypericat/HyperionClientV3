package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.commands.CommandHandlerDispatcher;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.RecievePacketListener;
import me.hypericats.hyperionclientv3.events.SendPacketListener;
import me.hypericats.hyperionclientv3.events.eventData.RecievePacketData;
import me.hypericats.hyperionclientv3.events.eventData.SendPacketData;
import me.hypericats.hyperionclientv3.mixinInterface.IClientConnection;
import me.hypericats.hyperionclientv3.modules.NoSlow;
import me.hypericats.hyperionclientv3.util.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Shadow protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At(value = "HEAD"), method = "render")
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!this.chatField.getText().startsWith(CommandHandlerDispatcher.getCommandNominator())) return;
        RenderUtil.fillWithBorder(context, 2, this.height - 14, this.width - 2, this.height -2, 0, ColorHelper.Argb.getArgb(255, 0, 255, 255), 1);
    }
}