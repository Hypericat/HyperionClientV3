package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.modules.*;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModuleHandler {
    private static final HashMap<Class<?>, Module> modules = new HashMap<>();
    private static KeyInputHandler inputHandler;
    public static void initModules() {
        if (!modules.isEmpty()) return;
        inputHandler = new KeyInputHandler();

        initModule(new Flight());
        initModule(new KillAura());
        initModule(new Criticals());
        initModule(new Fullbright());
        initModule(new HackGUI());
        initModule(new InvWalk());
        initModule(new VClip());
        initModule(new InfAura());
        initModule(new Freecam());
        initModule(new NoFall());
        initModule(new VanillaSpoofer());
        initModule(new FastBreak());
        initModule(new AntiEffect());
        initModule(new NoOverlay());
        initModule(new LedgeProtection());
        initModule(new Velocity());
        initModule(new Blink());
        initModule(new Friends());
        initModule(new PlayerEsp());
        initModule(new HostileEntityEsp());
        initModule(new PassiveEntityEsp());
        initModule(new ItemEntityEsp());
        initModule(new ServerPlayerPacketBlocker());
        initModule(new NoSlow());
        initModule(new Zoom());
        initModule(new FastPlace());
        initModule(new AutoHit());
        initModule(new AutoTool());
        initModule(new Speed());
        initModule(new SmallHandRenderer());
        initModule(new BiggerHitboxes());
        initModule(new Jesus());
        initModule(new WaypointHandler());
        initModule(new BlockForceResourcePacks());
        initModule(new ForceRenderBarriers());
        initModule(new Nuker());
        initModule(new InvPortal());
        initModule(new BlockEntityEsp());
        initModule(new Xray());
    }

    private static void initModule(Module module) {
        modules.put(module.getClass(), module);
    }

    public static<T extends Module> Module getModuleByClass(Class<T> clss) {
        return modules.get(clss);
    }
    public static Module getModuleByName(String name) {
        for (Module m : modules.values()) {
            if (m.getName().equalsIgnoreCase(name)) return m;
        }
        return null;
    }
    public static List<Module> getModules() {
        return modules.values().stream().toList();
    }
    public static<T extends Module>  boolean isModuleEnable(Class<T> clss) {
        Module module = getModuleByClass(clss);
        if (module == null) return false;
        return module.isEnabled();
    }

    //Perhaps use hashmap?
    public static List<Module> getModulesByKeybind(InputUtil.Key key) {
        List<Module> mod = new ArrayList<>();
        for (Module m : modules.values()) {
            if (m.getKey() == null) continue;
            if (m.getKey().getTranslationKey().equalsIgnoreCase(key.getTranslationKey())) mod.add(m);
        }
        return mod;
    }
}
