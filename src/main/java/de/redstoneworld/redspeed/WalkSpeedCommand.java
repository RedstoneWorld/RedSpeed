package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class WalkSpeedCommand {

	protected final RedSpeed plugin;
	private Player player;
	private Player sender;
	private String targetName;
	
	public static final float DEFAULT_WALK_SPEED = (float) 0.2;
	public static final float MAX_WALK_SPEED = (float) 1;

	/**
	 * @param plugin the plugin
	 * @param player the player there execute the command
	 */
	public WalkSpeedCommand(RedSpeed plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	/**
	 * @param plugin the plugin
	 * @param sender the player there execute the command
	 * @param target additional command arguments (= the target player)
	 */
	public WalkSpeedCommand(RedSpeed plugin, Player sender, String target) {
		this.plugin = plugin;
		this.sender = sender;
		this.targetName = target;
	}

	// normal player commands:
	
	/**
	 * This method allow to SHOW the own walk speed value, if the executor has the permission to
	 * use the command.
	 */
	public boolean sendWalkSpeedMsg() {

		if (!hasSeePermission()) {
			return true;
		}

		float ws = player.getWalkSpeed();
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("show.wspeed-own", "wspeed", String.valueOf(ws)));
		return true;

	}

	/**
	 * This method allow to SET the own walk speed value back to the default value, if the executer
	 * has the permission to use the command.
	 */
	public boolean setDefaultSpeed() {

		if (!hasSetPermission()) {
			return true;
		}

		player.setWalkSpeed(DEFAULT_WALK_SPEED);
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET the own walk speed value to the requested value, if the executor
	 * has the permission to use the command.
	 * 
	 * @param speed the requested walk speed value
	 */
	public boolean setSpecificSpeed(float speed) {
		
		if (!hasSetPermission() || !checkValue(speed)) {
			return true;
		}

		player.setWalkSpeed(speed);
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.ownSpecificValue", "wspeed", String.valueOf(speed)));
		return true;

	}
	
	// admin commands:
	
	/**
	 * This method allow to SHOW the walk speed value of another player, if the executor has
	 * the permission to use the command. The target player must be online.
	 */
	public boolean sendWalkSpeedMsgOther() {
		
		if (!hasSeeOtherPermission() || !isOnline()) {
			return true;
		}

		float ws = player.getWalkSpeed();
		sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("show.wspeed-other", "name", targetName, "wspeed", String.valueOf(ws)));
		return true;

	}

	/**
	 * This method allow to SET the walk speed value of another player back to the default value,
	 * if the executer has the permission to use the command. The target player must be online.
	 */
	public boolean setDefaultSpeedOther() {

		if (!hasSetOtherPermission() || !isOnline()) {
			return true;
		}
		
		player.setWalkSpeed(DEFAULT_WALK_SPEED);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.other", "name", targetName, "wspeed", String.valueOf(DEFAULT_WALK_SPEED)));
		}
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET the walk speed value of another player to the requested value,
	 * if the executor has the permission to use the command. The target player must be online.
	 * 
	 * @param speed the requested walk speed value
	 */
	public boolean setSpecificSpeedOther(float speed) {
		
		if (!hasSetOtherPermission() || !isOnline() || !checkValue(speed)) {
			return true;
		}

		player.setWalkSpeed(speed);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.other", "name", targetName, "wspeed", String.valueOf(speed)));
		}
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-wspeed.ownSpecificValue", "wspeed", String.valueOf(speed)));
		return true;

	}
	
	// permission check:
	
	public boolean hasSeePermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.see")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSetPermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.set")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSeeOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.see.other")) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSetOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.set.other")) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	/**
	 * This method check if the player is online on the server.
	 * 
	 * @return "true" if the player is online on the server
	 */
	public boolean isOnline() {
		this.player = (Player) plugin.getServer().getPlayerExact(targetName);
		if (this.player == null) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("player-not-found", "name", targetName));
			return false;
		}
		return true;
	}
	
	/**
	 * This method check the value of requested walk speed value.
	 * 
	 * @param speed the requested walk speed value
	 * @return "true" if the requested speed value is correct (inside in the allowed range)
	 */
	public boolean checkValue(float speed) {
		if ( speed < 0.0 || speed > MAX_WALK_SPEED ) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue.wspeed"));
			return false;
		}
		return true;
	}

}