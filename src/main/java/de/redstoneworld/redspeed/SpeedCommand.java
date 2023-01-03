package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public abstract class SpeedCommand {

    protected final RedSpeed plugin;
    private Player player;
    private Player sender;
    private String targetName;
    
    /** the default value for the specified speed-type */
    public static float defaultSpeed = 0;

    /** the max value for the specified speed-type */
    public static float maxSpeed = 0;
    
    /** the name of the specified speed-type */
    protected static String speedType = "";

    /** the name of placeholder for the speed value of specified speed-type messages */
    protected static String speedPlaceholder = "";
    
    
    /**
     * @param plugin the plugin
     * @param player the player there execute the command
     */
    public SpeedCommand(RedSpeed plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    /**
     * @param plugin the plugin
     * @param sender the player there execute the command
     * @param target additional command arguments (= the target player)
     */
    public SpeedCommand(RedSpeed plugin, Player sender, String target) {
        this.plugin = plugin;
        this.sender = sender;
        this.targetName = target;
    }
    
    // normal player commands:

    /**
     * This method allows to SHOW the own walk / fly speed value, if the executor has the permission to
     * use the command.
     */
    boolean sendSpeedMsg() {

        if (!hasSeePermission()) {
            return true;
        }

        float speed = getSpeed(player);
        player.sendMessage(plugin.getLang("prefix") 
                + plugin.getLang("show." + speedType + "-own", speedPlaceholder, String.valueOf(speed)));
        return true;
    }

    /**
     * This method allows to SET the own walk / fly speed value back to the default value, if the executor
     * has the permission to use the command.
     */
    boolean setDefaultSpeed() {

        if (!hasSetPermission()) {
            return true;
        }
        
        setSpeed(player, defaultSpeed);
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-" + speedType + ".ownDefaultValue"));
        return true;
    }

    /**
     * This method allows to SET the own walk / fly speed value to the requested value, if the executor
     * has the permission to use the command.
     *
     * @param speed the requested walk / fly speed value
     */
    boolean setSpecificSpeed(float speed) {

        if (!hasSetPermission() || !checkValue(speed)) {
            return true;
        }
        
        setSpeed(player, speed);
        player.sendMessage(plugin.getLang("prefix") 
                + plugin.getLang("success-" + speedType + ".ownSpecificValue", speedPlaceholder, String.valueOf(speed)));
        return true;
    }

    // admin commands:

    /**
     * This method allows to SHOW the walk / fly speed value of another player, if the executor has
     * the permission to use the command. The target player must be online.
     */
    boolean sendSpeedMsgOther() {

        if (!hasSeeOtherPermission() || !isOnline()) {
            return true;
        }

        float speed = getSpeed(player);
        sender.sendMessage(plugin.getLang("prefix") 
                + plugin.getLang("show." + speedType + "-other", "name", targetName, speedPlaceholder, String.valueOf(speed)));
        return true;
    }

    /**
     * This method allows to SET the walk / fly speed value of another player back to the default value,
     * if the executor has the permission to use the command. The target player must be online.
     */
    boolean setDefaultSpeedOther() {

        if (!hasSetOtherPermission() || !isOnline()) {
            return true;
        }

        setSpeed(player, defaultSpeed);
        if (sender != player) {
            sender.sendMessage(plugin.getLang("prefix") 
                    + plugin.getLang("success-" + speedType + ".other", "name", targetName, speedPlaceholder, String.valueOf(defaultSpeed)));
        }
        player.sendMessage(plugin.getLang("prefix") + plugin.getLang("success-" + speedType + ".ownDefaultValue"));
        return true;
    }

    /**
     * This method allows to SET the walk / fly speed value of another player to the requested value,
     * if the executor has the permission to use the command. The target player must be online.
     *
     * @param speed the requested walk / fly speed value
     */
    boolean setSpecificSpeedOther(float speed) {

        if (!hasSetOtherPermission() || !isOnline() || !checkValue(speed)) {
            return true;
        }
        
        setSpeed(player, speed);
        if (sender != player) {
            sender.sendMessage(plugin.getLang("prefix") 
                    + plugin.getLang("success-" + speedType + ".other", "name", targetName, speedPlaceholder, String.valueOf(speed)));
        }
        player.sendMessage(plugin.getLang("prefix") 
                + plugin.getLang("success-" + speedType + ".ownSpecificValue", speedPlaceholder, String.valueOf(speed)));
        return true;
    }

    /**
     * This method allows to get the walk / fly speed command syntax if the player has the
     * basic use permission.
     */
    void sendSyntaxHelpMessage() {

        if (hasUsePermission()) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("syntaxError." + speedType));
        }
    }

    // permission check:

    public boolean hasUsePermission() {

        if (!player.hasPermission("rwm.redspeed." + speedType + ".use")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSeePermission() {

        if (!player.hasPermission("rwm.redspeed." + speedType + ".see")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSetPermission() {

        if (!player.hasPermission("rwm.redspeed." + speedType + ".set")) {
            player.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSeeOtherPermission() {

        if (!sender.hasPermission("rwm.redspeed." + speedType + ".see.other")) {
            sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("noPermission"));
            return false;
        }
        return true;
    }

    public boolean hasSetOtherPermission() {

        if (!sender.hasPermission("rwm.redspeed." + speedType + ".set.other")) {
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
    private boolean isOnline() {
        this.player = plugin.getServer().getPlayerExact(targetName);
        
        if (this.player == null) {
            sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("player-not-found", "name", targetName));
            return false;
        }
        return true;
    }
    
    /**
     * This method checks the value of requested walk / fly speed value.
     *
     * @param speed the requested walk / fly speed value
     * @return "true" if the requested speed value is correct (inside in the allowed range)
     */
    private boolean checkValue(float speed) {
        if (speed < 0.0 || speed > maxSpeed) {

            if (sender != null) {
                sender.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue." + speedType));
            } else {
                player.sendMessage(plugin.getLang("prefix") + plugin.getLang("wrongValue." + speedType));
            }
            return false;
        }
        return true;
    }
    
    /**
     * This method sets the default values for the specific type of speed.
     */
    public void setDefaultValues() {
    }

    /**
     * This method sets the new speed value for the player on the server.
     * 
     * @param player (Player) the target player
     * @param speed (float) the new speed value
     */
    protected void setSpeed(Player player, float speed) {
    }

    /**
     * This method gets the current speed value of the player on the server.
     *
     * @param player (Player) the target player
     * @return the current speed value
     */
    protected float getSpeed(Player player) {
        return 0;
    }
    
}