package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.auth.SessionHandler;
import me.hypericats.hyperionclientv3.commands.CommandHandlerDispatcher;
import me.hypericats.hyperionclientv3.gui.HyperionClientV3Screen;
import me.hypericats.hyperionclientv3.mixin.SessionAccessor;
import me.hypericats.hyperionclientv3.util.FileUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.Clipboard;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Uuids;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.logging.FileHandler;

public class HyperionClientV3Client implements ClientModInitializer {

	public static final String MODID = "hyperionclientv3";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final String defaultName = "User";
	public static final String windowTitle = "HyperionClientV3";
	public static KeybindLoader keybindLoader;
	public static ModuleSettingsSaver settingsSaver;
	public static HyperionClientV3Screen hyperionClientV3Screen;
	private static KeyBinding hyperionClientMenuKey;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

	}

	// Expensive don't spam, cache if needed
	public static boolean getDevState() {
		return new File(FileUtil.HypCv3Dir.getAbsolutePath() + "\\dev").isFile();
	}

	public static void actuallyInit() {
		LOGGER.info("Initializing HyperionClientV3");
		FileUtil.createDir(FileUtil.HypCv3Dir);
		if (getDevState()) {
			SessionHandler.setSession(SessionHandler.getCrackedSession(defaultName));
		}

		CommandHandlerDispatcher.initCommands();
		keybindLoader = new KeybindLoader();
		ModuleHandler.initModules();
		keybindLoader.loadKeys();
		ModuleSaver moduleSaver = new ModuleSaver();
		moduleSaver.loadModules();
		settingsSaver = new ModuleSettingsSaver();
		settingsSaver.loadSettings();
		hyperionClientV3Screen = new HyperionClientV3Screen();
		WaypointSaver waypointSaver = new WaypointSaver();
		waypointSaver.init();
	}

	protected static void registerMenuKeybind() {
		hyperionClientMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key." + MODID + ".hyperionClientMenu",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"key.category." + MODID + ".hyperion"
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if(hyperionClientMenuKey.wasPressed()) {
				HyperionClientV3Client.hyperionClientV3Screen.setScreen();
			}
		});
	}
	//infina jump
	//ghost block
	//auto totem

	//slow attack speed rendering

	//block esp / chest esp
	//fix custom names on player entities (npcs)
	//open inv in portals
	//gui screen black instead of blue
	//when speed and pos gets reset matrices get warped or something and cant move head knida snaps to axis when moving fast horizontally vertical is fine
	//fix forcerenderbarriers


	//Todo
	//Use hashmap for ModuleHandler.getByKeybind();



	//to fix TODO

}