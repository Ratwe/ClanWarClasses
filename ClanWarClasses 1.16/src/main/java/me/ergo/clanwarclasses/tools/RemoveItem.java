package me.ergo.clanwarclasses.tools;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RemoveItem {
    public void removeItem(Player player, ItemStack item, int amount){
        Inventory inventory = player.getInventory();
        for(ItemStack i: inventory.getContents()){
            if(i != null){
                int amount_i = i.getAmount();
                if(i == item){
                    if(i.getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName())){
                        if(amount_i > amount)
                            i.setAmount(amount_i - amount);
                        else if(amount_i == amount)
                            player.getInventory().remove(i);
                    }
                }
            }
        }
    }
}
