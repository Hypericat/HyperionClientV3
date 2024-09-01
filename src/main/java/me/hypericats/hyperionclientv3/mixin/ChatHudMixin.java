package me.hypericats.hyperionclientv3.mixin;

import me.hypericats.hyperionclientv3.mixinInterface.IChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.CharacterVisitor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.List;

@Mixin(net.minecraft.client.gui.hud.ChatHud.class)
public abstract class ChatHudMixin implements IChatHud {
    @Shadow protected abstract int getMessageIndex(double chatLineX, double chatLineY);

    @Shadow protected abstract double toChatLineX(double x);

    @Shadow protected abstract double toChatLineY(double y);

    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;

    @ModifyConstant(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", constant = @Constant(intValue = 100), expect = 2)
    public int getMaxHistory(int original) {
        return 16384;
    }

    public String getMessageAtCords(double x, double y) {
        int index = this.getMessageIndex(this.toChatLineX(x), this.toChatLineY(y));
        if (index == -1) return null;
        if (index + 1 > this.visibleMessages.size()) return null;

        StringBuilder msg = new StringBuilder();
        ChatHudLine.Visible line = this.visibleMessages.get(index);
        CharacterVisitor visitor = (index1, style, codePoint) -> {
            msg.append((char)codePoint);
            return true;
        };
        line.content().accept(visitor);
        return (msg.toString());
    }
}