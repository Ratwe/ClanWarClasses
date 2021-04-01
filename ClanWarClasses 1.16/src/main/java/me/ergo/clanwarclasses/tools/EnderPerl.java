package me.ergo.clanwarclasses.tools;

import me.ergo.clanwarclasses.ClanWarClasses;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static java.lang.Math.*;

public class EnderPerl implements Listener {
    ClanWarClasses plugin;
    public  EnderPerl(ClanWarClasses plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void throwPerl(PlayerInteractEvent event){
        ItemStack item1 = event.getPlayer().getInventory().getItemInMainHand();
        ItemStack item2 = event.getPlayer().getInventory().getItemInOffHand();

        if((item1.getType() == Material.ENDER_PEARL || item2.getType() == Material.ENDER_PEARL) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location loc_block =  event.getClickedBlock().getLocation();
            Location loc_p =  event.getPlayer().getLocation();

            if(abs(loc_block.getX()-floor(loc_p.getX())) + abs(loc_block.getZ()-floor(loc_p.getZ())) <= 1 && abs(loc_block.getY()-round(loc_p.getY()-1))<4) {
                event.getPlayer().sendMessage(ChatColor.RED + "Вы не можете кинуть жемчуг края в блок на таком близком расстоянии");
                event.setCancelled(true);
            }
        }
    }
}
