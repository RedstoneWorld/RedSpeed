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
	 * This method allow to SHOW the own fly speed value.
	 */
	public boolean sendFlySpeedMsg() {

		if (!hasSeePermission()) {
			return true;
		}

		float fs = player.getFlySpeed();
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("show.fspeed-own", "fspeed", String.valueOf(fs)));
		return true;

	}

	/**
	 * This method allow to SET the own fly speed value to the default value.
	 */
	public boolean setDefaultSpeed() {

		if (!hasSetPermission()) {
			return true;
		}

		player.setFlySpeed(DEFAULT_FLY_SPEED);
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-fspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET the own fly speed value to the requested value.
	 */
	public boolean setSpecificSpeed(float x) {

		if (!hasSetPermission() || !checkValue(x)) {
			return true;
		}

		player.setFlySpeed(x);
		player.sendMessage(plugin.getLang("präfix")
				+ plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(x)));
		return true;

	}

	// admin commands:

	/**
	 * This method allow to SHOW fly speed value of another player.
	 */
	public boolean sendFlySpeedMsgOther() {

		if (!hasSeeOtherPermission() || !isOnline()) {
			return true;
		}

		float fs = player.getFlySpeed();
		sender.sendMessage(plugin.getLang("präfix")
				+ plugin.getLang("show.fspeed-other", "name", targetName, "fspeed", String.valueOf(fs)));
		return true;

	}

	/**
	 * This method allow to SET fly speed value of another player to the default
	 * value.
	 */
	public boolean setDefaultSpeedOther() {

		if (!hasSetOtherPermission() || !isOnline()) {
			return true;
		}

		player.setFlySpeed(DEFAULT_FLY_SPEED);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-fspeed.other", "name", targetName,
					"fspeed", String.valueOf(DEFAULT_FLY_SPEED)));
		}
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-fspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET fly speed value of another player to the requested
	 * value.
	 */
	public boolean setSpecificSpeedOther(float x) {

		if (!hasSetOtherPermission() || !isOnline() || !checkValue(x)) {
			return true;
		}

		player.setFlySpeed(x);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("präfix")
					+ plugin.getLang("success-fspeed.other", "name", targetName, "fspeed", String.valueOf(x)));
		}
		player.sendMessage(plugin.getLang("präfix")
				+ plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(x)));
		return true;

	}

	// permission check:

	public boolean hasSeePermission() {

		if (!player.hasPermission("rwm.redspeed.fspeed.see")) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	public boolean hasSetPermission() {

		if (!player.hasPermission("rwm.redspeed.fspeed.set")) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	public boolean hasSeeOtherPermission() {

		if (!sender.hasPermission("rwm.redspeed.fspeed.see.other")) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	public boolean hasSetOtherPermission() {

		if (!sender.hasPermission("rwm.redspeed.fspeed.set.other")) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}

	/**
	 * This method check if the player is online on the server. The user must online
	 * to SHOW or SET the fly speed value of him.
	 */
	public boolean isOnline() {
		this.player = (Player) plugin.getServer().getPlayerExact(targetName);
		if (this.player == null) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("player-not-found", "name", targetName));
			return false;
		}
		return true;
	}

	/**
	 * This method check the value of requested fly speed value.
	 */
	public boolean checkValue(float speed) {
		if (speed < 0.0 || speed > MAX_FLY_SPEED) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("wrongValue.fspeed"));
			return false;
		}
		return true;
	}

}