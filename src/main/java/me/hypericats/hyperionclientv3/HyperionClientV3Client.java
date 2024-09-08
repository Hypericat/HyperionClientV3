package me.hypericats.hyperionclientv3;

import me.hypericats.hyperionclientv3.commands.CommandHandlerDispatcher;
import me.hypericats.hyperionclientv3.gui.HyperionClientV3Screen;
import me.hypericats.hyperionclientv3.util.FileUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HyperionClientV3Client implements ClientModInitializer {

	public static final String MODID = "hyperionclientv3";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	public static final String defaultName = "User";
	public static final boolean isDev = true;
	public static KeybindLoader keybindLoader;
	public static ModuleSettingsSaver settingsSaver;
	public static HyperionClientV3Screen hyperionClientV3Screen;
	private static KeyBinding hyperionClientMenuKey;

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.


	}

	public static void actuallyInit() {
		SharedConstants.isDevelopment = isDev;
		LOGGER.info("Initializing HyperionClientV3");
		FileUtil.createDir(FileUtil.HypCv3Dir);
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