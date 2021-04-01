package me.ergo.clanwarclasses.tools;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown {
    public static int SecLeft (Long get_id, int cooldownTime){
        return (int) ((get_id/1000 + cooldownTime) - System.currentTimeMillis()/1000);
    }
}
