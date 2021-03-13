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
	 * This method allow to SHOW the own
	 * walk speed value.
	 */
	public boolean sendWalkSpeedMsg() {

		if (!hasSeePermission()) {
			return true;
		}

		float ws = player.getWalkSpeed();
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("show.wspeed-own", "wspeed", String.valueOf(ws)));
		return true;

	}

	/**
	 * This method allow to SET the own
	 * walk value to the default value.
	 */
	public boolean setDefaultSpeed() {

		if (!hasSetPermission()) {
			return true;
		}

		player.setWalkSpeed(DEFAULT_WALK_SPEED);
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET the own
	 * walk speed value to the requested value.
	 */
	public boolean setSpecificSpeed(float x) {
		
		if (!hasSetPermission() || !checkValue(x)) {
			return true;
		}

		player.setWalkSpeed(x);
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.ownSpecificValue", "wspeed", String.valueOf(x)));
		return true;

	}
	
	// admin commands:
	
	/**
	 * This method allow to SHOW walk speed value
	 * of another player.
	 */
	public boolean sendWalkSpeedMsgOther() {
		
		if (!hasSeeOtherPermission() || !isOnline()) {
			return true;
		}

		float ws = player.getWalkSpeed();
		sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("show.wspeed-other", "name", targetName, "wspeed", String.valueOf(ws)));
		return true;

	}

	/**
	 * This method allow to SET walk speed value
	 * of another player to the default value.
	 */
	public boolean setDefaultSpeedOther() {

		if (!hasSetOtherPermission() || !isOnline()) {
			return true;
		}
		
		player.setWalkSpeed(DEFAULT_WALK_SPEED);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.other", "name", targetName, "wspeed", String.valueOf(DEFAULT_WALK_SPEED)));
		}
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.ownDefaultValue"));
		return true;

	}

	/**
	 * This method allow to SET walk speed value
	 * of another player to the requested value.
	 */
	public boolean setSpecificSpeedOther(float x) {
		
		if (!hasSetOtherPermission() || !isOnline() || !checkValue(x)) {
			return true;
		}

		player.setWalkSpeed(x);
		if (sender != player) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.other", "name", targetName, "wspeed", String.valueOf(x)));
		}
		player.sendMessage(plugin.getLang("präfix") + plugin.getLang("success-wspeed.ownSpecificValue", "wspeed", String.valueOf(x)));
		return true;

	}
	
	// permission check:
	
	public boolean hasSeePermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.see")) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSetPermission() {
		
		if (!player.hasPermission("rwm.redspeed.wspeed.set")) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSeeOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.see.other")) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	public boolean hasSetOtherPermission() {
		
		if (!sender.hasPermission("rwm.redspeed.wspeed.set.other")) {
			sender.sendMessage(plugin.getLang("präfix") + plugin.getLang("noPermission"));
			return false;
		}
		return true;
	}
	
	/**
	 * This method check if the player is online
	 * on the server. The user must online to
	 * SHOW or SET the walk speed value of him.
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
	 * This method check the value of requested
	 * walk speed value.
	 */
	public boolean checkValue(float speed) {
		if ( speed < 0.0 || speed > MAX_WALK_SPEED ) {
			player.sendMessage(plugin.getLang("präfix") + plugin.getLang("wrongValue.wspeed"));
			return false;
		}
		return true;
	}

}