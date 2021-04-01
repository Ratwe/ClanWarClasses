package me.ergo.clanwarclasses.tools;

import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.exp.ExpManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.UUID;

public class RestorePotionEffects implements Listener {
    ClanWarClasses plugin;
    public RestorePotionEffects(ClanWarClasses plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void death (PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

        if (user.inGroup("archer"))
            if (ExpManager.getPlayerExp(player).lvl >= 30)
                    new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(PotionEffectType.SPEED.createEffect(60000, 0));
                    }
                }.runTaskLater(this.plugin, 2);


        if (user.inGroup("assassin"))
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(60000, 0));
                }
            }.runTaskLater(this.plugin, 2);


        if(user.inGroup("tank"))
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.addPotionEffect(PotionEffectType.SLOW.createEffect(60000, 0));
                }
            }.runTaskLater(this.plugin, 2);
    }


    @EventHandler
    public void milk (PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if(item.getType() == Material.MILK_BUCKET) {
            Player player = event.getPlayer();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            if (user.inGroup("archer"))
                if (ExpManager.getPlayerExp(player).lvl >= 30)
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.addPotionEffect(PotionEffectType.SPEED.createEffect(60000, 0));
                        }
                    }.runTaskLater(this.plugin, 2);

            if (user.inGroup("assassin"))
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(60000, 0));
                    }
                }.runTaskLater(this.plugin, 2);

            if(user.inGroup("tank"))
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(PotionEffectType.SLOW.createEffect(60000, 0));
                    }
                }.runTaskLater(this.plugin, 2);
        }
    }
}
