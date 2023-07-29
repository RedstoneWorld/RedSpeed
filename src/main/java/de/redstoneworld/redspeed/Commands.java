package de.redstoneworld.redspeed;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Commands implements TabExecutor {

	protected final RedSpeed plugin;

	public Commands(RedSpeed plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {

		/*
		 * This command allows showing / edit the walk player speed.
		 * 
		 * ingame command syntax: /redwalkspeed [walk-speed | 'back'] [player]
		 */

		if (cmd.getName().equalsIgnoreCase("redwalkspeed")) {
			
			Player player;
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("onlyIngame"));
				return true;
			}

			player = (Player) sender;
			

			// without arguments --> show walk-speed value
			if (args.length == 0) {
				WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
				walkSpeedCmd.sendSpeedMsg();
				return true;
			}

			if (args.length == 1) {

				// e.g. "/wspeed back"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
					walkSpeedCmd.setDefaultSpeed();
					return true;

				} else {
					// e.g. "/wspeed 1"
					if (isSpeedArgument(args[0])) {
						float speed = getSpeedValue(args[0]);

						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
						walkSpeedCmd.setSpecificSpeed(speed);
						return true;

					} else {
						// e.g. "/wspeed TestUser"
						// if x is a string (player name) = admin commands
						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player, args[0]);
						walkSpeedCmd.sendSpeedMsgOther();
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

				} else {
					// e.g. "/wspeed 1 TestUser"
					if (isSpeedArgument(args[0])) {
						float speed = getSpeedValue(args[0]);

						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player, args[1]);
						walkSpeedCmd.setSpecificSpeedOther(speed);
						return true;

					} else {
						WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
						walkSpeedCmd.sendSyntaxHelpMessage();
						return true;
					}
				}
			}

			WalkSpeedCommand walkSpeedCmd = new WalkSpeedCommand(plugin, player);
			walkSpeedCmd.sendSyntaxHelpMessage();
			return true;

		}

		/*
		 * This command allows showing / edit the fly player speed.
		 * 
		 * ingame command syntax: /redflyspeed [fly-speed | 'back'] [player]
		 */

		if (cmd.getName().equalsIgnoreCase("redflyspeed")) {
			
			Player player;
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("onlyIngame"));
				return true;
			}

			player = (Player) sender;
			

			// without arguments --> show fly-speed value
			if (args.length == 0) {
				FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
				flySpeedCmd.sendSpeedMsg();
				return true;
			}

			if (args.length == 1) {

				// e.g. "/fspeed back"
				if (args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
					FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
					flySpeedCmd.setDefaultSpeed();
					return true;

				} else {
					// e.g. "/fspeed 1"
					if (isSpeedArgument(args[0])) {
						float speed = getSpeedValue(args[0]);

						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
						flySpeedCmd.setSpecificSpeed(speed);
						return true;

					} else {
						// "/fspeed TestUser"
						// if x is a string (player name) = admin commands
						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player, args[0]);
						flySpeedCmd.sendSpeedMsgOther();
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

				} else {
					// e.g. "/fspeed 1 TestUser"
					if (isSpeedArgument(args[0])) {
						float speed = getSpeedValue(args[0]);

						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player, args[1]);
						flySpeedCmd.setSpecificSpeedOther(speed);
						return true;

					} else {
						FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
						flySpeedCmd.sendSyntaxHelpMessage();
						return true;
					}
				}
			}

			FlySpeedCommand flySpeedCmd = new FlySpeedCommand(plugin, player);
			flySpeedCmd.sendSyntaxHelpMessage();
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
	private boolean isSpeedArgument(String cmdInput) {

		if (cmdInput.matches("^[-\\+]?[0-9]*[\\.,]?[0-9]+$")) {
			return true;
		}

		return false;

	}


	/**
	 * This method reforms the speed spelling and convert it to a float number.
	 *
	 * @param cmdInput the decimal value argument with one of all supported spellings
	 * @return (float) the decimal value
	 */
	private float getSpeedValue(String cmdInput) {

		// replace decimal separator
		cmdInput = cmdInput.replace(",", ".");

		// replace decimal input as short write-style without zero at beginning
		// (e.g. "/speed .4" --> "/speed 0.4")
		if (cmdInput.matches("^[-\\+]?[\\.,][0-9]+$")) {
			cmdInput = cmdInput.replace(".", "0.");
		}

		// convert the string to a float number
		return Float.parseFloat(cmdInput);

	}


	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

		Player player;
		List<String> completions = new ArrayList<>();
		final List<String> commands = new ArrayList<>();

		if (cmd.getName().equalsIgnoreCase("redwalkspeed")) {

			// Console
			if (!(sender instanceof Player)) {
				return null;

			} else {

				// normal player
				player = (Player) sender;

				if (args.length == 1) {
					if ((player.hasPermission("rwm.redspeed.wspeed.set")) || (player.hasPermission("rwm.redspeed.wspeed.set.other"))) {
						commands.add("back");
					}
					if (player.hasPermission("rwm.redspeed.wspeed.see.other")) {
						Bukkit.getOnlinePlayers().forEach(onlinePlayer -> commands.add(onlinePlayer.getName()));
					}
					StringUtil.copyPartialMatches(args[0], commands, completions);
				}

				if (args.length == 2) {
					if ((isSpeedArgument(args[0])) || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
						if (player.hasPermission("rwm.redspeed.wspeed.set.other")) {
							Bukkit.getOnlinePlayers().forEach(onlinePlayer -> commands.add(onlinePlayer.getName()));
						}
					}
					StringUtil.copyPartialMatches(args[1], commands, completions);
				}

			}

		}

		if (cmd.getName().equalsIgnoreCase("redflyspeed")) {

			// Console
			if (!(sender instanceof Player)) {
				return null;

			} else {

				// normal player
				player = (Player) sender;

				if (args.length == 1) {
					if ((player.hasPermission("rwm.redspeed.fspeed.set")) || (player.hasPermission("rwm.redspeed.fspeed.set.other"))) {
						commands.add("back");
					}
					if (player.hasPermission("rwm.redspeed.fspeed.see.other")) {
						Bukkit.getOnlinePlayers().forEach(onlinePlayer -> commands.add(onlinePlayer.getName()));
					}
					StringUtil.copyPartialMatches(args[0], commands, completions);
				}

				if (args.length == 2) {
					if ((isSpeedArgument(args[0])) || args[0].equalsIgnoreCase("back") || args[0].equalsIgnoreCase("default")) {
						if (player.hasPermission("rwm.redspeed.fspeed.set.other")) {
							Bukkit.getOnlinePlayers().forEach(onlinePlayer -> commands.add(onlinePlayer.getName()));
						}
					}
					StringUtil.copyPartialMatches(args[1], commands, completions);
				}

			}

		}

		return completions;
	}

}