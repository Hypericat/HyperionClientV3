package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ScheduleStopListener;
import me.hypericats.hyperionclientv3.util.FileUtil;
import net.minecraft.client.util.InputUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class KeybindLoader implements ScheduleStopListener {
    public static File keybindDir = new File(FileUtil.HypCv3Dir.getAbsolutePath() + "\\Keybinds");
    public KeybindLoader() {
        initDir();
        EventHandler.register(ScheduleStopListener.class, this);
    }

    public void initDir() {
        if (!keybindDir.exists()) keybindDir.mkdirs();
    }
    public void saveKeys() {
        initDir();
        if (keybindDir.listFiles() != null) {
            for (File file : Objects.requireNonNull(keybindDir.listFiles())) {
                if (file.getName().endsWith(".keyb")) file.delete();
            }
        }
        InputUtil.Key key;
        for (Module mod : ModuleHandler.getModules()) {
            key = mod.getKey();
            if (key == null) continue;
            File keyb = FileUtil.createFile(mod.getName() + ".keyb", keybindDir);
            FileUtil.overwriteToFile(mod.getKey().getTranslationKey(), keyb);
        }
    }
    public void loadKeys() {
        if (keybindDir.listFiles() == null) return;
        for (File file : Objects.requireNonNull(keybindDir.listFiles())) {
            String filename = file.getName();
            if (!filename.endsWith(".keyb")) return;
            filename = filename.replace(".keyb", "");
            Module mod = ModuleHandler.getModuleByName(filename);
            if (mod == null) return;
            List<String> fileContents = FileUtil.readFromFile(file);
            if (fileContents == null || fileContents.isEmpty() || fileContents.get(0).equals("0")) return;
            InputUtil.Key key = InputUtil.fromTranslationKey(fileContents.get(0));
            if (key == null) return;
            mod.setKey(key, false);
        }
    }

    @Override
    public void onEvent(EventData data) {
        saveKeys();
    }
}
