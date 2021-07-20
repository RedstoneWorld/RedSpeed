package de.redstoneworld.redspeed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyCommandExecutor implements CommandExecutor {

	protected final RedSpeed plugin;

	public MyCommandExecutor(RedSpeed plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		/**
		 * This command allow to show / edit the walk player speed.
		 * 
		 * ingame command syntax: /redwalkspeed [walk-speed | 'back'] [player]
		 */

		if (cmd.getName().equalsIgnoreCase("redwalkspeed")) {
			
			Player player = null;
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("onlyIngame"));
				return true;
			}

			player = (Player) sender;
			

			// without arguments --> show walk-speed value
			if (args.length == 0) {
				WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
				walkSpeedCmd.sendWalkSpeedMsg();
				return true;
			}

			if (args.length == 1) {

				// e.g. "/wspeed back"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
					walkSpeedCmd.setDefaultSpeed();
					return true;
				}

				else {
					// e.g. "/wspeed 1"
					if (matchSpeedArgument(args[0])) {
						String matchedString = (args[0]).replaceFirst(",", ".");
						float speed = Float.parseFloat(matchedString);

						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
						walkSpeedCmd.setSpecificSpeed(speed);
						return true;

					} else {
						// e.g. "/wspeed TestUser"
						// if x is a string (player name) = admin commands
						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player, args[0]);
						walkSpeedCmd.sendWalkSpeedMsgOther();
						return true;
					}
				}
			}

			// = admin commands :
			if (args.length == 2) {

				// e.g. "/wspeed back TestUser"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player, args[1]);
					walkSpeedCmd.setDefaultSpeedOther();
					return true;
				}

				else {
					// e.g. "/wspeed 1 TestUser"
					if (matchSpeedArgument(args[0])) {
						String matchedString = (args[0]).replaceFirst(",", ".");
						float speed = Float.parseFloat(matchedString);

						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player, args[1]);
						walkSpeedCmd.setSpecificSpeedOther(speed);
						return true;

					} else {
						player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError.wspeed"));
						return true;
					}
				}
			}

			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError.wspeed"));
			return true;

		}

		/**
		 * This command allow to show / edit the fly player speed.
		 * 
		 * ingame command syntax: /redflyspeed [fly-speed | 'back'] [player]
		 */

		if (cmd.getName().equalsIgnoreCase("redflyspeed")) {
			
			Player player = null;
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("onlyIngame"));
				return true;
			}

			player = (Player) sender;
			

			// without arguments --> show fly-speed value
			if (args.length == 0) {
				FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
				flySpeedCmd.sendFlySpeedMsg();
				return true;
			}

			if (args.length == 1) {

				// e.g. "/fspeed back"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
					flySpeedCmd.setDefaultSpeed();
					return true;
				}

				else {
					// e.g. "/fspeed 1"
					if (matchSpeedArgument(args[0])) {
						String matchedString = (args[0]).replaceFirst(",", ".");
						float speed = Float.parseFloat(matchedString);

						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
						flySpeedCmd.setSpecificSpeed(speed);
						return true;

					} else {
						// "/fspeed TestUser"
						// if x is a string (player name) = admin commands
						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player, args[0]);
						flySpeedCmd.sendFlySpeedMsgOther();
						return true;
					}
				}
			}

			// = admin commands :
			if (args.length == 2) {

				// "/fspeed back TestUser"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player, args[1]);
					flySpeedCmd.setDefaultSpeedOther();
					return true;
				}

				else {
					// e.g. "/fspeed 1 TestUser"
					if (matchSpeedArgument(args[0])) {
						String matchedString = (args[0]).replaceFirst(",", ".");
						float speed = Float.parseFloat(matchedString);

						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player, args[1]);
						flySpeedCmd.setSpecificSpeedOther(speed);
						return true;

					} else {
						player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError.fspeed"));
						return true;
					}
				}
			}

			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError.fspeed"));
			return true;

		}

		return false;

	}

	/**
	 * This method validate the input string and decide between a string (= playername)
	 * and a number (= speed value).
	 * 
	 * @return 'true' if the input is a rationale number
	 */
	public boolean matchSpeedArgument(String cmdInput) {
		if (cmdInput.matches("^[-]?[0-9]+([\\.,][0-9])?[0-9]*$")) {
			return true;
		}
		return false;
	}

}
