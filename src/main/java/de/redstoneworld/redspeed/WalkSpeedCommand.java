package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class WalkSpeedCommand {

	protected final RedSpeed plugin;
	private Player player;
	private Player sender;
	private String targetName;
	
	public static final float DEFAULT_WALK_SPEED = (float) 0.2;
	public static final float MAX_WALK_SPEED = (float) 1;

	public WalkSpeedCommand(RedSpeed plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	public WalkSpeedCommand(RedSpeed plugin, Player sender, String target) {
		this.plugin = plugin;
		this.sender = sender;
		this.targetName = target;
	}

	// normal player commands:

	/**
	 * Shows the walk speed value of the player.
	 *
	 * When the operation was successful the player will get a message.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If the operation was successful.
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
	 * Sets the own walk speed value to the default value.
	 *
	 * When the operation was successful the player will get a message.
	 *
	 * @return If the operation was successful.
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
	 * Sets the walk speed value of the sender to the requested value.
	 *
	 * When the operation was successful the sender will get a message.
	 *
	 * @param speed The speed that the player should be set.
	 * @return If the operation was successful.
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
	 * Shows the walk speed value of another player to the sender.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * When the operation was successful the sender will get a message.
	 *
	 * @return If the operation was successful.
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
	 * Sets the walk speed value of another player to the default value.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * When the operation was successful the sender and the player will get a message.
	 *
	 * @return If this operation was successful.
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
	 * Sets the walk speed value of the player to the requested value.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * When the operation was successful the sender and the player will get a message.
	 *
	 * @param speed The speed that the player should be set.
	 * @return If this operation was successful.
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

	/**
	 * Returns a boolean that shows if the player has the permission to see the walk speed of himself.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSeePermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.see")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the player has the permission to change the walk speed of himself.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSetPermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.set")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the sender has the permission to see the walk-speed of other players.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSeeOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.see.other")) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the sender has the permission to set the walk-speed of other players.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSetOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.set.other")) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the player is online on the server.
	 * The user must be online to SHOW or SET the fly speed value.
	 *
	 * When the targeted player is not online the sender will get a message.
	 *
	 * @return If the player is online or not.
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
	 * Returns a boolean that shows if the requested walk speed value is correct.
	 *
	 * When the value is not correct the targeted player will get a message.
	 *
	 * @param speed The speed of the player.
	 * @return If the walk speed value is correct.
	 */
	public boolean checkValue(float speed) {
		if ( speed < 0.0 || speed > MAX_WALK_SPEED ) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue.wspeed"));
			return false;
		}
		return true;
	}

}