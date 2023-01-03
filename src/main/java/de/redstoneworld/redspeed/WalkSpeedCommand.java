package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class WalkSpeedCommand extends SpeedCommand {

    public WalkSpeedCommand(RedSpeed plugin, Player player) {
        super(plugin, player);
        setDefaultValues();
    }

    public WalkSpeedCommand(RedSpeed plugin, Player sender, String target) {
        super(plugin, sender, target);
        setDefaultValues();
    }
    
    @Override
    public void setDefaultValues() {
        defaultSpeed = (float) 0.2;
        maxSpeed = (float) 1;
        speedType = "wspeed";
        speedPlaceholder = speedType;
    }
    
    @Override
    protected void setSpeed(Player player, float speed) {
        player.setWalkSpeed(speed);
    }

    @Override
    protected float getSpeed(Player player) {
        return player.getWalkSpeed();
    }
    
}