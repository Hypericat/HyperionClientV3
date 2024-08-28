package me.hypericats.hyperionclientv3.modules;

import me.hypericats.hyperionclientv3.HackType;
import me.hypericats.hyperionclientv3.Module;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.SoundHandler;
import me.hypericats.hyperionclientv3.moduleOptions.NumberOption;
import me.hypericats.hyperionclientv3.util.ChatUtils;
import me.hypericats.hyperionclientv3.util.PlayerUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class Friends extends Module {
    private final List<String> friendNames = new ArrayList<>();
    public Friends() {
        super(false);
    }
    @Override
    public void onEnable() {
        toggleEntityOverCursor();
        this.disable();
    }
    public List<String> getFriendList() {
        return friendNames;
    }
    public static boolean isFriend(Entity entity) {
        if (entity == null) return false;
        if (!(entity instanceof PlayerEntity player)) return false;
        if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.getId() == player.getId()) return false;
        return isFriend(player.getName());
    }
    public static boolean isFriend(String name) {
        Friends friends = (Friends) ModuleHandler.getModuleByClass(Friends.class);
        if (friends == null) return false;
        return friends.getFriendList().contains(name);
    }
    public static boolean isFriend(Text name) {
        return isFriend(name.getString());
    }
    private void toggleEntityOverCursor() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        HitResult hit = client.crosshairTarget;
        if (hit == null) return;
        if (hit.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult hitResult = (EntityHitResult) hit;
        Entity entity = hitResult.getEntity();

        if (!(entity instanceof PlayerEntity friend)) return;
        if (friend.getId() == client.player.getId()) return;
        toggleFriend(friend.getName().getString());
    }
    private void toggleFriend(String name) {
        if (friendNames.contains(name)) {
            friendNames.remove(name);
            ChatUtils.sendOfficial("&&4Removed &&ffriend &&a" + name);
            SoundHandler.playSound(SoundEvents.BLOCK_ANVIL_DESTROY);
            return;
        }
        friendNames.add(name);
        ChatUtils.sendOfficial("&&2Added &&ffriend &&a" + name);
        SoundHandler.playSound(SoundEvents.BLOCK_CHEST_OPEN);
    }

    @Override
    protected void initOptions() {

    }
    @Override
    public void onDisable() {

    }
    @Override
    public boolean shouldDisplayChatMessage() {
        return false;
    }
    @Override
    public String getName() {
        return "Friend Cursor";
    }

    @Override
    public HackType getHackType() {
        return HackType.COMBAT;
    }

    public String[] getAlias() {
        return new String[] {"Friend", "Exclude", "Don't Hit"};
    }
}
