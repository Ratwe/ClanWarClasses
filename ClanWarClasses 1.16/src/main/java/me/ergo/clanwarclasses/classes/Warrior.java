package me.ergo.clanwarclasses.classes;

import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.tools.ActionBar;
import me.ergo.clanwarclasses.tools.Cooldown;
import me.ergo.clanwarclasses.tools.PEX;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;

import static java.lang.Math.min;

public class Warrior implements Listener {
    // регистрация класса
    ClanWarClasses plugin;
    public Warrior(ClanWarClasses plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }



    /** Рывок **/
    final HashMap<UUID, Long> cooldownDash = new HashMap<>();
    int cooldownTimeDash = 7;

    // в archer'е есть метод, отнимающий флай после 2 секунд как человек прыгнул

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player.getName());
        if(user.inGroup("warrior") && player.getGameMode() == GameMode.SURVIVAL) {

            if (!cooldownDash.containsKey(id))
                cooldownDash.put(id, System.currentTimeMillis() - cooldownTimeDash * 1000L);

            int secondsLeft = (int) (((cooldownDash.get(player.getUniqueId()) / 1000) + cooldownTimeDash) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка

            if (secondsLeft <= 0) {
                cooldownDash.put(player.getUniqueId(), System.currentTimeMillis()); //обновляем кулдаун

                Location loc = player.getLocation().clone();
                Vector jump = loc.getDirection();
                player.setVelocity(jump.multiply(1));
                player.setAllowFlight(false);
                player.setHealth(min(player.getHealth() + 2, player.getHealthScale())); //восстановление одного сердца

                new BukkitRunnable() { //сообщение о конце перезарядки
                    @Override
                    public void run() {
                        ActionBar.send(player, ChatColor.GREEN + "Рывок перезарядился!");
                        player.setAllowFlight(true);
                    }
                }.runTaskLater(this.plugin, cooldownTimeDash * 20L); //через cooldownTime секунд
            }

            if (player.getGameMode() == GameMode.SURVIVAL)
                player.setFlying(false);
            event.setCancelled(true);
        }
    }


    /** Пробитие блока **/
    HashMap<UUID, Long> cooldownShieldBreak = new HashMap<>();

    @EventHandler
    public void shieldBreak(EntityDamageByEntityEvent event){
        int cooldownTime = 6;

        if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER){
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            UUID id = damager.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            if(user.inGroup("warrior")){
                if(!cooldownShieldBreak.containsKey(id))
                    cooldownShieldBreak.put(id, System.currentTimeMillis()-cooldownTime*1000);

                int secondsLeft = (int) (((cooldownShieldBreak.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000));
                if(secondsLeft <= 0) {
                    ItemStack item = victim.getInventory().getItemInOffHand(); // если у жертвы в левой руке щит
                    if (item.getType() == Material.SHIELD) {
                        victim.getInventory().remove(item); // удаляем щит

                        new BukkitRunnable() { // и возвращаем его через мгновение
                            @Override
                            public void run() {
                                victim.getInventory().setItemInOffHand(item);
                            }
                        }.runTaskLater(this.plugin, 5L);

                        cooldownShieldBreak.put(id, System.currentTimeMillis());
                    }
                }
            }
        }
    }


    /** Усиление **/
    HashMap<UUID, Long> cooldownGetPower = new HashMap<>();

    @EventHandler
    public void powerUp (PlayerInteractEvent event) {
        int cooldownTime = 15;
        int duration = 10;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

        if (user.inGroup("warrior")) {
            ItemStack item = p.getInventory().getItemInMainHand();
            List<Material> swords = new ArrayList<>(Arrays.asList(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD));

            if (swords.contains(item.getType()) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (!cooldownGetPower.containsKey(id))
                    cooldownGetPower.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                int secondsLeft = (int) ((((cooldownGetPower.get(id)) / 1000) + cooldownTime) - System.currentTimeMillis() / 1000);
                if (secondsLeft <= 0) {
                    p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(duration * 20, 0));
                    p.addPotionEffect(PotionEffectType.REGENERATION.createEffect(duration * 20, 0));
                    cooldownGetPower.put(id, System.currentTimeMillis());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ActionBar.send(p, ChatColor.GREEN + "Усиление перезарядилось!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime * 20L);
                }
            }
        }
    }


    /** Иммунитет к дебаффам **/
    HashMap<UUID, Long> cooldownImmunity = new HashMap<>();
    List<PotionEffectType> debuffs = new ArrayList<>(Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.BAD_OMEN, PotionEffectType.CONFUSION,
            /*PotionEffectType.GLOWING,*/ PotionEffectType.HUNGER, PotionEffectType.LEVITATION, PotionEffectType.POISON, PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING, PotionEffectType.UNLUCK, PotionEffectType.WEAKNESS, PotionEffectType.WITHER));

    // лист и метод названы одинаково!
    @EventHandler
    public void immunity(PlayerInteractEvent event) {
        int cooldownTime = 15;
        int duration = 10;

        Player p = event.getPlayer();
        UUID id = p.getUniqueId();
        if(PEX.inGroup(p, "warrior")){
            if(event.getMaterial() == Material.GLOWSTONE_DUST){
                if(!cooldownImmunity.containsKey(id))
                    cooldownImmunity.put(id, System.currentTimeMillis() - cooldownTime*1000);

                int secondsLeft = Cooldown.SecLeft(cooldownImmunity.get(id), cooldownTime+duration);
                if(secondsLeft <= 0) {
                    cooldownImmunity.put(id, System.currentTimeMillis());
                    p.addPotionEffect(PotionEffectType.GLOWING.createEffect(duration * 20, 0));

                    int time = 0;
                    while(time < duration*20) {
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                for (PotionEffectType a : debuffs) {
                                    p.removePotionEffect(a);
                                }
                            }
                        }.runTaskLater(this.plugin, time);

                        time+=5;
                    }

                    new BukkitRunnable(){
                        public void run(){
                            ActionBar.send(p, ChatColor.GREEN + "Иммунитет перезарядился!");
                        }
                    }.runTaskLater(this.plugin, (cooldownTime+duration)*20);

                }
            }
        }
    }


    /** Режим берсерка **/
    HashMap<UUID, Long> cooldownBerserkMode = new HashMap<>();

    @EventHandler
    public void berserkMode(EntityDamageEvent event) {
        int cooldownTime = 20;
        int duration = 10;

        if(event.getEntity().getType() == EntityType.PLAYER){
            Player p = (Player) event.getEntity();
            UUID id = p.getUniqueId();
            if(PEX.inGroup(p, "warrior")){
                double afterHP = p.getHealth() - event.getFinalDamage();
                if(afterHP <= p.getHealthScale() * 0.3 && afterHP > 0){
                    if(!cooldownBerserkMode.containsKey(id))
                        cooldownBerserkMode.put(id, System.currentTimeMillis() - cooldownTime*1000);

                    int secLeft = Cooldown.SecLeft(cooldownBerserkMode.get(id), cooldownTime);
                    if(secLeft <= 0) {
                        cooldownBerserkMode.put(id, System.currentTimeMillis());
                        p.addPotionEffect(PotionEffectType.REGENERATION.createEffect(duration*20, 0));
                        p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(duration*20, 1));
                        p.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(duration*20, 1));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActionBar.send(p, ChatColor.GREEN + "Режим берсерка перезарядился!");
                            }
                        }.runTaskLater(this.plugin, cooldownTime*20);
                    }
                }
            }
        }
    }
}
