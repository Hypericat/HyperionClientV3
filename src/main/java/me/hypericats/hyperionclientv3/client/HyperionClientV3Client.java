package me.hypericats.hyperionclientv3.client;

import me.hypericats.hyperionclientv3.KeybindLoader;
import me.hypericats.hyperionclientv3.ModuleHandler;
import me.hypericats.hyperionclientv3.util.FileUtil;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperionClientV3Client implements ClientModInitializer {

    public static final String MODID = "hyperionclientv3";
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
    public static KeybindLoader keybindLoader;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }

    public static void actuallyInit() {
        LOGGER.info("Initializing HyperionClientV3");
        FileUtil.createDir(FileUtil.HypCv3Dir);
        keybindLoader = new KeybindLoader();
        ModuleHandler.initModules();
        keybindLoader.loadKeys();

    }

    //Todo
    //Use hashmap for ModuleHandler.getByKeybind();
    //sometimes inf aura not hitting entities under roofs when standing in certain positions
}
