package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.modules.*;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.List;

public class ModuleHandler {
    private static final List<Module> modules = new ArrayList<>();
    private static KeyInputHandler inputHandler;
    public static void initModules() {
        if (!modules.isEmpty()) return;
        inputHandler = new KeyInputHandler();

        modules.add(new Flight());
        modules.add(new KillAura());
        modules.add(new Criticals());
        modules.add(new Fullbright());
        modules.add(new HackGUI());
        modules.add(new InvWalk());
        modules.add(new VClip());
        modules.add(new InfAura());
        modules.add(new Freecam());
        modules.add(new NoFall());
        modules.add(new VanillaSpoofer());
        modules.add(new FastBreak());

    }
    public static<T extends Module> Module getModuleByClass(Class<T> clss) {
        for (Module m : modules) {
            if (clss.isInstance(m)) return m;
        }
        return null;
    }
    public static Module getModuleByName(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
    public static List<Module> getModules() {
        return modules;
    }


    //Perhaps use hashmap?
    public static List<Module> getModulesByKeybind(InputUtil.Key key) {
        List<Module> mod = new ArrayList<>();
        for (Module m : modules) {
            if (m.getKey() == null) continue;
            if (m.getKey().getTranslationKey().equalsIgnoreCase(key.getTranslationKey())) mod.add(m);
        }
        return mod;
    }
}
