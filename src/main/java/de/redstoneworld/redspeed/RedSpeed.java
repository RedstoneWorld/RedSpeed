/**
 * This is a simple Minecraft Speed-Plugin to allow a changing of
 * own or other user speed for walk and fly.
 *
 * @author Robert Rauh alias RedstoneFuture
 */

package de.redstoneworld.redspeed;

import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class RedSpeed extends JavaPlugin {

	private MyCommandExecutor cmdExecutor;

	public void onEnable() {
		getLogger().info("[RedSpeed]: Das Plugin wurde aktiviert!");

		saveDefaultConfig();

		cmdExecutor = new MyCommandExecutor(this);
		// the synonym commands are defined with the plugin.yml
		getCommand("redwalkspeed").setExecutor(cmdExecutor);
		getCommand("redflyspeed").setExecutor(cmdExecutor);
	}

	public void onDisable() {
		getLogger().info("[RedSpeed]: Das Plugin wurde deaktiviert!");
	}

	/**
	 * This method read the specific messages in the config.yml and replace the
	 * minecraft color codes to a valid sign.
	 * 
	 * @param key YAML key
	 * @param String placeholder without "%"
	 * @param value value for the placeholder
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
