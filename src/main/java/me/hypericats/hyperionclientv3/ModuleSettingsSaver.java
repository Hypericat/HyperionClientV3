package me.hypericats.hyperionclientv3;

import com.google.gson.Gson;
import me.hypericats.hyperionclientv3.event.EventData;
import me.hypericats.hyperionclientv3.event.EventHandler;
import me.hypericats.hyperionclientv3.events.ModuleToggleListener;
import me.hypericats.hyperionclientv3.events.ScheduleStopListener;
import me.hypericats.hyperionclientv3.events.SettingsChangeListener;
import me.hypericats.hyperionclientv3.moduleOptions.EnumStringOption;
import me.hypericats.hyperionclientv3.moduleOptions.ModuleOption;
import me.hypericats.hyperionclientv3.util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ModuleSettingsSaver implements SettingsChangeListener, ScheduleStopListener, ModuleToggleListener {
    public static File hacksSettingsDir = new File(FileUtil.HypCv3Dir.getAbsolutePath() + "\\HaxSetting");
    public static final String splitter = "aaBB636312daspojdlasThisISASPLITTERDONTPUTTHISANYWHERE";
    public ModuleSettingsSaver() {
        initDirectory();
        EventHandler.register(ModuleToggleListener.class, this);
        EventHandler.register(ScheduleStopListener.class, this);
        EventHandler.register(SettingsChangeListener.class, this);
    }
    public void initDirectory() {
        FileUtil.HypCv3Dir.mkdirs();
        hacksSettingsDir.mkdirs();
    }
    public void onEvent(EventData data) {
        saveSettings();
    }
    public void saveSettings() {
        initDirectory();
        for (Module mod : ModuleHandler.getModules()) {
            File f = new File(hacksSettingsDir.getAbsolutePath() + "\\" + mod.getName() + ".hxset");
            FileUtil.createFile(f);
            FileUtil.clearFile(f);
            rightDataToFile(f, mod);
        }
    }
    private void rightDataToFile(File file, Module module) {
        Gson gson = new Gson();
        for (ModuleOption<?> option : module.getOptions().getAllOptions()) {
            if (!option.isShouldSave()) continue;
            String str = option.getName() + splitter;
            FileUtil.appendLineToFile(str + gson.toJson(option.getValue()) + '\n', file);
        }    }
    public void loadSettings() {
        if (hacksSettingsDir.listFiles() == null) return;
        for (File file : Objects.requireNonNull(hacksSettingsDir.listFiles())) {
            String fileName = file.getName();
            if (!fileName.endsWith(".hxset")) continue;
            fileName = fileName.replace(".hxset", "");
            Module mod = ModuleHandler.getModuleByName(fileName);
            if (mod == null) continue;
            List<String> jsons = FileUtil.readFromFile(file);
            if (jsons == null) return;
            Gson gson = new Gson();
            main : for (String json : jsons) {
                if (!json.contains(splitter)) continue;
                String jsonArray[] = json.split(splitter);
                if (jsonArray.length != 2) return;
                Object value = gson.fromJson(jsonArray[1], Object.class);
                ModuleOption<?> option = mod.getOptions().getOptionByName(jsonArray[0]);
                if (option instanceof EnumStringOption<?> eOption) {
                    for (Enum<?> e : eOption.getValue().getClass().getEnumConstants()) {
                        if (e.toString().equalsIgnoreCase(value.toString())) {
                            option.setValue(e, false);
                            continue main;
                        }
                    }
                    continue;
                }
                option.setValue(value, false);
            }
        }
    }
}
