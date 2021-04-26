package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class FlySpeedCommand {

	protected final RedSpeed plugin;
	private Player player;
	private Player sender;
	private String targetName;

	public static final float DEFAULT_FLY_SPEED = (float) 0.1;
	public static final float MAX_FLY_SPEED = (float) 1;

	public FlySpeedCommand(RedSpeed plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
	}

	public FlySpeedCommand(RedSpeed plugin, Player sender, String target) {
		this.plugin = plugin;
		this.sender = sender;
		this.targetName = target;
	}

	// normal player commands:

	/**
	 * Shows the fly speed value of the player.
	 *
	 * When the operation was successful the player will get a message.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If the operation was successful.
	 */
	public boolean sendFlySpeedMsg() {

		if (!hasSeePermission()) {
			return false;
		}

		float fs = player.getFlySpeed();
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("show.fspeed-own", "fspeed", String.valueOf(fs)));
		return true;

	}

	/**
	 * Sets the own fly speed value to the default value.
	 *
	 * When the operation was successful the player will get a message.
	 *
	 * @return If the operation was successful.
	 */
	public boolean setDefaultSpeed() {

		if (!hasSetPermission()) {
			return false;
		}

		player.setFlySpeed(DEFAULT_FLY_SPEED);
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * Sets the fly speed value of the sender to the requested value.
	 *
	 * When the operation was successful the sender will get a message.
	 *
	 * @param speed The speed that the player should be set.
	 * @return If the operation was successful.
	 */
	public boolean setSpecificSpeed(float speed) {

		if (!hasSetPermission() || !checkValue(speed)) {
			return false;
		}

		player.setFlySpeed(speed);
		player.sendMessage(plugin.getLang("prefix")
				+ plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(speed)));
		return true;

	}

	// admin commands:

	/**
	 * Shows the fly speed value of another player to the sender.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * When the operation was successful the sender will get a message.
	 *
	 * @return If the operation was successful.
	 */
	public boolean sendFlySpeedMsgOther() {

		if (!hasSeeOtherPermission() || !isOnline()) {
			return false;
		}

		float fs = player.getFlySpeed();
		sender.sendMessage(plugin.getLang("prefix")
				+ plugin.getLang("show.fspeed-other", "name", targetName, "fspeed", String.valueOf(fs)));
		return true;

	}

	/**
	 * Sets the fly speed value of another player to the default value.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * When the operation was successful the sender and the player will get a message.
	 *
	 * @return If this operation was successful.
	 */
	public boolean setDefaultSpeedOther() {

		if (!hasSetOtherPermission() || !isOnline()) {
			return false;
		}

		player.setFlySpeed(DEFAULT_FLY_SPEED);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.other", "name", targetName,
					"fspeed", String.valueOf(DEFAULT_FLY_SPEED)));
		}
		player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * Sets the fly speed value of the player to the requested value.
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
			return false;
		}

		player.setFlySpeed(speed);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("prefix")
					+ plugin.getLang("success-fspeed.other", "name", targetName, "fspeed", String.valueOf(speed)));
		}
		player.sendMessage(plugin.getLang("prefix")
				+ plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(speed)));
		return true;

	}

	// permission check:

	/**
	 * Returns a boolean that shows if the player has the permission to see the fly speed of himself.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSeePermission() {

		if (!player.hasPermission("rwm.redspeed.fspeed.see")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the player has the permission to change the fly speed of himself.
	 *
	 * When the player does not have the permission the player will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSetPermission() {

		if (!player.hasPermission("rwm.redspeed.fspeed.set")) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the sender has the permission to see the fly-speed of other players.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSeeOtherPermission() {

		if (!sender.hasPermission("rwm.redspeed.fspeed.see.other")) {
			sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * Returns a boolean that shows if the sender has the permission to set the fly-speed of other players.
	 *
	 * When the sender does not have the permission the sender will get a message.
	 *
	 * @return If player has permission.
	 */
	public boolean hasSetOtherPermission() {

		if (!sender.hasPermission("rwm.redspeed.fspeed.set.other")) {
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
	 * Returns a boolean that shows if the requested fly speed value is correct.
	 *
	 * When the value is not correct the targeted player will get a message.
	 *
	 * @param speed The speed of the player.
	 * @return If the fly speed value is correct.
	 */
	public boolean checkValue(float speed) {
		if (speed < 0.0 || speed > MAX_FLY_SPEED) {
			player.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue.fspeed"));
			return false;
		}
		return true;
	}

}