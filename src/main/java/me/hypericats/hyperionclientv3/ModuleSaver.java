package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ModuleToggleListener;
import me.hypericats.hyperionclientv3.events.ScheduleStopListener;
import me.hypericats.hyperionclientv3.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleSaver implements ModuleToggleListener, ScheduleStopListener {
    public static File hacksDir = new File(FileUtil.HypCv3Dir.getAbsolutePath() + "\\Hax");
    public ModuleSaver() {
        initDirectory();
        EventHandler.register(ModuleToggleListener.class, this);
        EventHandler.register(ScheduleStopListener.class, this);
    }
    public void initDirectory() {
        FileUtil.HypCv3Dir.mkdirs();
        hacksDir.mkdirs();
    }
    public void onEvent(EventData data) {
        initDirectory();
        List<File> files = new ArrayList<>();
        for (Module mod : ModuleHandler.getModules()) {
            if (mod.shouldSaveState() && mod.isEnabled()) files.add(new File(hacksDir.getAbsolutePath() + "\\" + mod.getName() + ".hax"));
        }
        dir : for (File f : Objects.requireNonNull(hacksDir.listFiles())) {
            for (File file : files) {
                if (file.getAbsolutePath().equals(f.getAbsolutePath())) continue dir;
            }
            f.delete();
        }
        for (File f : files) {
            FileUtil.createFile(f);
        }

    }
    public void loadModules() {
        if (hacksDir.listFiles() == null) return;
        for (File file : Objects.requireNonNull(hacksDir.listFiles())) {
            String fileName = file.getName();
            if (!fileName.endsWith(".hax")) continue;
            fileName = fileName.replace(".hax", "");
            Module mod = ModuleHandler.getModuleByName(fileName);
            if (mod == null) continue;
            mod.enable();
        }
    }
}
