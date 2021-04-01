package me.ergo.clanwarclasses.commands;

import me.ergo.clanwarclasses.exp.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class Stats implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            UUID id = player.getUniqueId();

            HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
            ExpPlayer expPlayer = map.get(id);
            if (!map.containsKey(id)) {
                MapPut.value = new ExpPlayer();
                MapPut.key = id;
                map = new ExpPlayerMap().getMap();
            }

            expPlayer = map.get(id);

            player.sendMessage(ChatColor.DARK_RED + "Статистика игрока " + ChatColor.YELLOW + player.getName());
            player.sendMessage(ChatColor.RED + "Уровень: " + ChatColor.WHITE + expPlayer.lvl);
            player.sendMessage(ChatColor.RED + "Опыт: " + ChatColor.WHITE + expPlayer.exp + '/' + expPlayer.new_lvl_exp);
        }

        return true;
    }


}
