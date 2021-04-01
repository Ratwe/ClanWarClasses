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

public class SetLevel implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);

        if(!sender.hasPermission("clanwarclasses.setlevel")) {
            sender.sendMessage(ChatColor.RED + "У вас недостаточно прав!");
            return true;
        }
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
        int amount = Integer.parseInt(args[1]);

        HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
        if (!map.containsKey(id))
            map.put(id, new ExpPlayer());

        ExpPlayer expPlayer = map.get(id);

        double was = player.getHealthScale();
        double now = was;

        if(user.inGroup("tank"))
            now = 20 + 2 * amount / 10;
        else
            now = 20 + 2 * amount / 20;

        player.setHealthScale(now);
        if(was < now)
            player.sendMessage(ChatColor.YELLOW + "Ваше здоровье увеличилось до " + now + '!');
        else if(was > now)
            player.sendMessage(ChatColor.YELLOW + "Ваше здоровье уменьшилось до " + now + '!');
        else
            player.sendMessage(ChatColor.YELLOW + "Ваше здоровье не изменилось.");


        if(amount > expPlayer.lvl)
            player.sendMessage(ChatColor.YELLOW + sender.getName() + " повысил ваш уровень до " + amount + '!');
        else
            player.sendMessage(ChatColor.YELLOW + sender.getName() + " понизил ваш уровень до " + amount + '!');

        expPlayer.lvl = amount;
        expPlayer.exp = 0;
        expPlayer.new_lvl_exp = 10 * pow(1.1, amount);

        map.put(id, expPlayer);
        MapPut.key = id;
        MapPut.value = expPlayer;
        map = new ExpPlayerMap().getMap();

        return true;
    }
}
