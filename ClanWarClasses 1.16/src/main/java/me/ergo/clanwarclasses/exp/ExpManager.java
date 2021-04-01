package me.ergo.clanwarclasses.exp;

import me.ergo.clanwarclasses.ClanWarClasses;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static java.lang.Math.*;

public class ExpManager implements Listener {
    public HashMap<UUID, ExpPlayer> map = new HashMap<>();

    public ExpManager(ClanWarClasses plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void mobDeath(EntityDeathEvent event){
        Player killer = event.getEntity().getKiller();
        if(killer != null) {
            int amount = event.getDroppedExp();
            UUID id = Objects.requireNonNull(killer).getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(killer.getName());

            killer.sendMessage("Вы получили " + amount + " опыта");

            if(!map.containsKey(id))
                map.put(id, new ExpPlayer());

            ExpPlayer expPlayer = map.get(id);
            expPlayer.exp += amount;
            while(expPlayer.exp >= expPlayer.new_lvl_exp){
                expPlayer.lvl++;
                expPlayer.exp -= expPlayer.new_lvl_exp;
                expPlayer.new_lvl_exp = round(pow(1.1, expPlayer.lvl-1) * 10);
                killer.sendMessage(ChatColor.YELLOW + "Вы повысили свой уровень до " + expPlayer.lvl + '!');
                if(expPlayer.lvl%20==0) {
                    killer.setHealthScale(killer.getHealthScale() + 2); // увеличиываем ха на сердце
                    killer.sendMessage(ChatColor.YELLOW + "Ваше здоровье возросло до " + killer.getHealthScale() + '.');
                }
                else if(user.inGroup("tank") && expPlayer.lvl%10==0) {
                    killer.setHealthScale(killer.getHealthScale() + 2);
                    killer.sendMessage(ChatColor.YELLOW + "Ваше здоровье возросло до " + killer.getHealthScale() + '.');
                }
            }

            expPlayer = map.get(id);
            map.put(id, expPlayer);
            MapPut.key = id;
            MapPut.value = expPlayer;
            map = new ExpPlayerMap().getMap();
        }
    }


    @EventHandler
    public void playerDeath(PlayerDeathEvent event){
        Player killer = event.getEntity().getKiller();
        if(killer != null && killer != event.getEntity().getPlayer()){
            int amount = min(event.getDroppedExp(), 30);
            UUID id = Objects.requireNonNull(killer).getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(killer.getName());

            killer.sendMessage("Вы получили " + amount + " опыта за убийство игрока " + Objects.requireNonNull(event.getEntity().getPlayer()).getName());

            if(!map.containsKey(id))
                map.put(id, new ExpPlayer());

            ExpPlayer expPlayer = map.get(id);
            expPlayer.exp += amount;
            while(expPlayer.exp >= expPlayer.new_lvl_exp){
                expPlayer.lvl++;
                expPlayer.exp -= expPlayer.new_lvl_exp;
                killer.sendMessage(ChatColor.YELLOW + "Вы повысили свой уровень до " + expPlayer.lvl + '!');
                if(expPlayer.lvl%20==0) {
                    killer.setHealthScale(killer.getHealthScale() + 2); // увеличиываем ха на сердце
                    killer.sendMessage(ChatColor.YELLOW + "Ваше здоровье возросло до " + killer.getHealthScale() + '.');
                }
                else if(user.inGroup("tank") && expPlayer.lvl%10==0) {
                    killer.setHealthScale(killer.getHealthScale() + 2);
                    killer.sendMessage(ChatColor.YELLOW + "Ваше здоровье возросло до " + killer.getHealthScale() + '.');
                }
            }

            map.put(id, expPlayer);
            MapPut.key = id;
            MapPut.value = expPlayer;
            map = new ExpPlayerMap().getMap();
        }
    }

    public static ExpPlayer getPlayerExp(Player player){
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
        HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();

        if(!map.containsKey(id))
            map.put(id, new ExpPlayer());

        return map.get(id);
    }
}

