package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.gui.HyperionClientV3Screen;
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
		ModuleSaver moduleSaver = new ModuleSaver();
		moduleSaver.loadModules();
		HyperionClientV3Screen hyperionClientV3Screen = new HyperionClientV3Screen();

	}

	//Todo
	//Use hashmap for ModuleHandler.getByKeybind();
	//Add border to ToggleOptionsWidgets
	//get the widest option in the column of options instead of adding 200

}