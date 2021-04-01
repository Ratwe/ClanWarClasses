package me.ergo.clanwarclasses.commands;

import me.ergo.clanwarclasses.exp.ExpPlayer;
import me.ergo.clanwarclasses.exp.ExpPlayerMap;
import me.ergo.clanwarclasses.exp.MapPut;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.UUID;

import static java.lang.Math.pow;
import static java.lang.Math.round;

public class GiveXP implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);

        if(!sender.hasPermission("clanwarclasses.givexp")){
            sender.sendMessage(ChatColor.RED + "У вас недостаточно прав.");
            return true;
        }
        if(player == null){
            sender.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
        int amount = Integer.parseInt(args[1]);

        HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
        if(!map.containsKey(id))
            map.put(id, new ExpPlayer());

        ExpPlayer expPlayer = map.get(id);
        expPlayer.exp += amount;

        player.sendMessage(ChatColor.YELLOW + "Игрок " + sender.getName() + " выдал вам " + amount + " опыта.");

        while(expPlayer.exp >= expPlayer.new_lvl_exp){
            expPlayer.lvl++;
            expPlayer.exp -= expPlayer.new_lvl_exp;
            expPlayer.new_lvl_exp = round(pow(1.1, expPlayer.lvl-1) * 10);
            player.sendMessage(ChatColor.YELLOW + "Ваш уровень повысился до " + expPlayer.lvl + '!');
            sender.sendMessage(ChatColor.YELLOW + "Вы повысили уровень " + player.getName() + " до " + expPlayer.lvl + '.');
            if(expPlayer.lvl%20==0) {
                player.setHealthScale(player.getHealthScale() + 2); // увеличиываем хп на сердце
                player.sendMessage(ChatColor.YELLOW + "Ваше здоровье увеличилось до " + player.getHealthScale() + '.');
            }
            else if(user.inGroup("tank") && expPlayer.lvl%10==0) {
                player.setHealthScale(player.getHealthScale() + 2);
                player.sendMessage(ChatColor.YELLOW + "Ваше здоровье увеличилось до " + player.getHealthScale() + '.');
            }
        }

        map.put(id, expPlayer);
        MapPut.key = id;
        MapPut.value = expPlayer;
        map = new ExpPlayerMap().getMap();

        return true;
    }
}
