/**
 * This is a simple Minecraft Speed-Plugin to allow a changing of
 * own or other user speed for walk and fly.
 *
 * Permissions:
 * rwm.redspeed.wspeed.(use|see|set)
 * rwm.redspeed.wspeed.(see|set).other
 * rwm.redspeed.fspeed.(use|see|set)
 * rwm.redspeed.fspeed.(see|set).other
 * 
 * @author Robert Rauh alias RedstoneFuture
 */

package de.redstoneworld.redspeed;

import de.redstoneworld.redutilities.files.bukkit.FileReader;
import org.bukkit.plugin.java.JavaPlugin;

public class RedSpeed extends JavaPlugin {

	public FileReader configReader = new FileReader();
	private Commands cmdExecutor;

	public void onEnable() {

		// save default config and load config
		saveDefaultConfig();
		reloadConfig();
		configReader.setConfig(getConfig());
		
		cmdExecutor = new Commands(this);
		// the synonym commands are defined with the plugin.yml
		getCommand("redwalkspeed").setExecutor(cmdExecutor);
		getCommand("redflyspeed").setExecutor(cmdExecutor);
	}

	public void onDisable() {
		
	}

	public FileReader getConfigReader() {
		return configReader;
	}
	
}
