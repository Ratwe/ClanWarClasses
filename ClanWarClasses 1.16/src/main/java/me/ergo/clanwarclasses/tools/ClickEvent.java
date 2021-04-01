package me.ergo.clanwarclasses.tools;

import me.ergo.clanwarclasses.ClanWarClasses;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent implements Listener {

    public ClickEvent(ClanWarClasses plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void guiClickEvent(InventoryClickEvent event){
        if(event.getView().getTitle().equalsIgnoreCase(ChatColor.BLACK + "Способности")) {
            /*switch (event.getCurrentItem().getType()){
                case BOW:

            }*/


            event.setCancelled(true);
        }
    }
}
