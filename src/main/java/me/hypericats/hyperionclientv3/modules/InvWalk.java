package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.enums.HacksScreenInputBlockBehaviour;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.TickListener;
import me.hypericats.hyperionclientv3.gui.HyperionClientV3Screen;
import me.hypericats.hyperionclientv3.gui.ModuleEditScreen;
import me.hypericats.hyperionclientv3.mixinInterface.IKeyBinding;
import me.hypericats.hyperionclientv3.moduleOptions.BooleanOption;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class InvWalk extends Module implements TickListener {
    public InvWalk() {
        super(true);
    }
    private BooleanOption blockShift;
    private EnumStringOption<HacksScreenInputBlockBehaviour> hacksScreenBehaviour;
    private BooleanOption blockCreativeScreen;
    private BooleanOption blockOnScreenWithTextBox;
    private ArrayList<KeyBinding> keys;

    @Override
    public void onEvent(EventData data) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        Screen screen = client.currentScreen;
        if (!(screen instanceof HyperionClientV3Screen || screen instanceof ModuleEditScreen || screen instanceof HandledScreen<?>)) return;
        if (blockCreativeScreen.getValue() && client.currentScreen instanceof CreativeInventoryScreen && isTextFieldWidgetFocused(client.currentScreen)) return;
        if (blockOnScreenWithTextBox.getValue() && isTextFieldWidgetFocused(client.currentScreen)) return;
        if (screen instanceof HyperionClientV3Screen && hacksScreenBehaviour.getValue() == HacksScreenInputBlockBehaviour.BLOCKMOVEMENT) return;
        for (KeyBinding key : keys) {
            if (key.getTranslationKey().equals(client.options.sneakKey.getTranslationKey()) && blockShift.getValue()) continue;

            ((IKeyBinding) key).resetPressedState();
        }
    }
    public boolean isTextFieldWidgetFocused(Screen screen) {
        for (Element i : screen.children()) {
            if (!(i instanceof TextFieldWidget widget)) continue;
            if (widget.isFocused() && widget.isActive() && widget.isVisible()) return true;
        }
        return false;
    }
    @Override
    public void onEnable() {
        EventHandler.register(TickListener.class, this);
    }
    public boolean shouldBlockHyperionHacksScreenTyping() {
        return this.isEnabled() && hacksScreenBehaviour.getValue() == HacksScreenInputBlockBehaviour.BLOCKTYPING;
    }

    @Override
    protected void initOptions() {
        MinecraftClient client = MinecraftClient.getInstance();
        keys = new ArrayList<>();
        keys.add(client.options.sneakKey);
        keys.add(client.options.sprintKey);
        keys.add(client.options.jumpKey);
        keys.add(client.options.forwardKey);
        keys.add(client.options.backKey);
        keys.add(client.options.leftKey);
        keys.add(client.options.rightKey);

        blockShift = new BooleanOption(true, "Block Sneaks", true);
        hacksScreenBehaviour = new EnumStringOption<>(true, "Hyperion Settings Behaviour", HacksScreenInputBlockBehaviour.BLOCKTYPING);
        blockCreativeScreen = new BooleanOption(true, "Disable in Creative Search", true);
        blockOnScreenWithTextBox = new BooleanOption(true, "Disable in Textbox Screen", true);

        options.addOption(blockShift);
        options.addOption(hacksScreenBehaviour);
        options.addOption(blockCreativeScreen);
        options.addOption(blockOnScreenWithTextBox);
    }
    @Override
    public void onDisable() {
        EventHandler.unregister(TickListener.class, this);
    }

    @Override
    public String getName() {
        return "InventoryWalk";
    }

    @Override
    public HackType getHackType() {
        return HackType.MOVEMENT;
    }

    public String[] getAlias() {
        return new String[] {"Gui Walk", "Inv Walk"};
    }
}
