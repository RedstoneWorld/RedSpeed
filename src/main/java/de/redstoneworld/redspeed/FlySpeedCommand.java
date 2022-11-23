package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class FlySpeedCommand {

    protected final RedSpeed plugin;
    private Player player;
    private Player sender;
    private String targetName;

    public static final float DEFAULT_FLY_SPEED = (float) 0.1;
    public static final float MAX_FLY_SPEED = (float) 1;

    /**
     * @param plugin the plugin
     * @param player the player there execute the command
     */
    public FlySpeedCommand(RedSpeed plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * @param plugin the plugin
     * @param sender the player there execute the command
     * @param target additional command arguments (= the target player)
     */
    public FlySpeedCommand(RedSpeed plugin, Player sender, String target) {
        this.plugin = plugin;
        this.sender = sender;
        this.targetName = target;
    }

    // normal player commands:

    /**
     * This method allows to SHOW the own fly speed value, if the executor has the permission to
     * use the command.
     */
    public boolean sendFlySpeedMsg() {

        if (!hasSeePermission()) {
            return true;
        }

        float fs = player.getFlySpeed();
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("show.fspeed-own", "fspeed", String.valueOf(fs)));
        return true;

    }

    /**
     * This method allows to SET the own fly speed value back to the default value, if the executor
     * has the permission to use the command.
     */
    public boolean setDefaultSpeed() {

        if (!hasSetPermission()) {
            return true;
        }

        player.setFlySpeed(DEFAULT_FLY_SPEED);
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.ownDefaultValue"));
        return true;

    }

    /**
     * This method allows to SET the own fly speed value to the requested value, if the executor
     * has the permission to use the command.
     *
     * @param speed the requested fly speed value
     */
    public boolean setSpecificSpeed(float speed) {

        if (!hasSetPermission() || !checkValue(speed)) {
            return true;
        }

        player.setFlySpeed(speed);
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(speed)));
        return true;

    }

    // admin commands:

    /**
     * This method allows to SHOW the fly speed value of another player, if the executor has
     * the permission to use the command. The target player must be online.
     */
    public boolean sendFlySpeedMsgOther() {

        if (!hasSeeOtherPermission() || !isOnline()) {
            return true;
        }

        float fs = player.getFlySpeed();
        sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("show.fspeed-other", "name", targetName,
                "fspeed", String.valueOf(fs)));
        return true;

    }

    /**
     * This method allows to SET the fly speed value of another player back to the default value,
     * if the executor has the permission to use the command. The target player must be online.
     */
    public boolean setDefaultSpeedOther() {

        if (!hasSetOtherPermission() || !isOnline()) {
            return true;
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
     * This method allows to SET the fly speed value of another player to the requested value,
     * if the executor has the permission to use the command. The target player must be online.
     *
     * @param speed the requested fly speed value
     */
    public boolean setSpecificSpeedOther(float speed) {

        if (!hasSetOtherPermission() || !isOnline() || !checkValue(speed)) {
            return true;
        }

        player.setFlySpeed(speed);
        if (sender != player) {
            sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.other", "name", targetName,
                    "fspeed", String.valueOf(speed)));
        }
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-fspeed.ownSpecificValue", "fspeed", String.valueOf(speed)));
        return true;

    }

    /**
     * This method allows to get the fly speed command syntax if the player has the
     * basic use permission.
     */
    public void sendSyntaxHelpMessage() {

        if (hasUsePermission()) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError.fspeed"));
        }
    }

    // permission check:

    public boolean hasUsePermission() {

        if (!player.hasPermission("rwm.redspeed.fspeed.use")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSeePermission() {

        if (!player.hasPermission("rwm.redspeed.fspeed.see")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSetPermission() {

        if (!player.hasPermission("rwm.redspeed.fspeed.set")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSeeOtherPermission() {

        if (!sender.hasPermission("rwm.redspeed.fspeed.see.other")) {
            sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSetOtherPermission() {

        if (!sender.hasPermission("rwm.redspeed.fspeed.set.other")) {
            sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    /**
     * This method checks if the player is online on the server.
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
     * This method checks the value of requested fly speed value.
     *
     * @param speed the requested fly speed value
     * @return "true" if the requested speed value is correct (inside in the allowed range)
     */
    public boolean checkValue(float speed) {
        if (speed < 0.0 || speed > MAX_FLY_SPEED) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue.fspeed"));
            return false;
        }
        return true;
    }

}