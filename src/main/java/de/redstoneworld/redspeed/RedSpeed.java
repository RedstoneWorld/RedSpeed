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

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class RedSpeed extends JavaPlugin {

	private MyCommandExecutor cmdExecutor;

	public void onEnable() {

		// save default config and load config
		saveDefaultConfig();
		reloadConfig();
		
		cmdExecutor = new MyCommandExecutor(this);
		// the synonym commands are defined with the plugin.yml
		getCommand("redwalkspeed").setExecutor(cmdExecutor);
		getCommand("redflyspeed").setExecutor(cmdExecutor);
	}

	public void onDisable() {
		
	}

	/**
	 * This method reads the specific messages in config.yml and replaces
	 * the minecraft color codes with a valid character.
	 *
	 * @param key YAML key
	 * @param args placeholder without "%" and value for the placeholder
	 *
	 * @return the config messages (String)
	 */
	String getLang(String key, String... args) {
		String lang = getConfig().getString("messages." + key, "&cUnknown language key &6" + key);
		for (int i = 0; i + 1 < args.length; i += 2) {
			lang = lang.replace("%" + args[i] + "%", args[i + 1]);
		}
		return ChatColor.translateAlternateColorCodes('&', lang);
	}

}
