package de.redstoneworld.redspeed;

import org.bukkit.entity.Player;

public class FlySpeedCommand extends SpeedCommand {

    public FlySpeedCommand(RedSpeed plugin, Player player) {
        super(plugin, player);
        setDefaultValues();
    }

    public FlySpeedCommand(RedSpeed plugin, Player sender, String target) {
        super(plugin, sender, target);
        setDefaultValues();
    }
    
    @Override
    public void setDefaultValues() {
        defaultSpeed = (float) 0.1;
        maxSpeed = (float) 1;
        speedType = "fspeed";
        speedPlaceholder = speedType;
    }
    
    @Override
    protected void setSpeed(Player player, float speed) {
        player.setFlySpeed(speed);
    }

    @Override
    protected float getSpeed(Player player) {
        return player.getFlySpeed();
    }
    
}