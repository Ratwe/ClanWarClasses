package me.ergo.clanwarclasses.classes;

import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.tools.ActionBar;
import me.ergo.clanwarclasses.tools.ClanTools;
import me.ergo.clanwarclasses.tools.Cooldown;
import me.ergo.clanwarclasses.tools.PEX;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Tank implements Listener {
    // регистрация класса
    private final ClanWarClasses plugin;
    public Tank(ClanWarClasses plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /** сбрасываются ВСЕ не положительные эффекты (кроме голода) и накладывается регенерация, сопротивление, огнестойкость, скорость **/
    HashMap<UUID, Long> cooldownResetAndBuff = new HashMap<>();


    /** При включённом скилле при атаке противник получает эффект отравления и слабости **/
    HashMap<UUID, Long> cooldownPoisonedSkin = new HashMap<>();
    List<UUID> poisonedSkin = new ArrayList<>();

    @EventHandler
    public void poisonedSkin (PlayerInteractEvent event){
        int cooldownTime = 10;
        int duration = 8;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if(PEX.inGroup(p, "tank")) {
            if (event.getMaterial() == Material.LEATHER) {
                if (!cooldownPoisonedSkin.containsKey(id))
                    cooldownPoisonedSkin.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                int secondsLeft = Cooldown.SecLeft(cooldownPoisonedSkin.get(id), cooldownTime);
                if (secondsLeft <= 0) {
                    cooldownPoisonedSkin.put(id, System.currentTimeMillis());
                    //p.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(duration*20, 0));
                    poisonedSkin.add(id);

                    new BukkitRunnable() {
                        public void run() {
                            poisonedSkin.remove(id);
                        }
                    }.runTaskLater(this.plugin, duration * 20);

                    new BukkitRunnable() {
                        public void run() {
                            ActionBar.send(p, ChatColor.GREEN + "Ядовитая кожа перезарядилась!");
                        }
                    }.runTaskLater(this.plugin, (duration+cooldownTime) * 20);
                }
            }
        }
    }

    @EventHandler
    public void poisonedSkinTouch (EntityDamageByEntityEvent event){
        int duration = 6;

        if(event.getEntity().getType() == EntityType.PLAYER && event.getDamager().getType() == EntityType.PLAYER) {
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            UUID id = victim.getUniqueId();

            if (poisonedSkin.contains(id)) {
                damager.addPotionEffect(PotionEffectType.POISON.createEffect(duration * 20, 0));
                damager.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(duration * 20, 1));
            }
        }
    }


    /** Притяжение **/
    HashMap<UUID, Long> cooldownAttraction = new HashMap<>();

    @EventHandler
    public void attraction (PlayerInteractEvent event){
        int cooldownTime = 10;
        int r = 10;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if(PEX.inGroup(p, "tank")) {
            if (event.getMaterial() == Material.SUGAR) {
                if (!cooldownAttraction.containsKey(id))
                    cooldownAttraction.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                int secondsLeft = Cooldown.SecLeft(cooldownAttraction.get(id), cooldownTime);
                if (secondsLeft <= 0) {
                    cooldownAttraction.put(id, System.currentTimeMillis());

                    Location pLoc = p.getLocation();
                    Vector pVector = pLoc.toVector();

                    List<Entity> list = p.getNearbyEntities(r, r, r);
                    for (Entity e : list) {
                        if (e.getType() == EntityType.PLAYER)
                            if (ClanTools.sameClan((Player) e, p))
                                continue;

                        Location eLoc = e.getLocation();
                        Vector eVector = eLoc.toVector();

                        Vector vector = eVector.subtract(pVector);
                        vector.normalize();
                        vector.multiply(new Vector(-2, -1.5, -2));

                        e.setVelocity(vector);
                    }

                    new BukkitRunnable() {
                        public void run() {
                            ActionBar.send(p, ChatColor.GREEN + "Притяжение перезарядилось!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime * 20);
                }
            }
        }
    }

    @EventHandler
    public void resetAndBuff(PlayerInteractEvent event){
        int cooldownTime = 20;
        int duration = 8;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

        if(user.inGroup("tank")){
            if(p.getInventory().getItemInMainHand().getType() == Material.APPLE && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!cooldownResetAndBuff.containsKey(id))
                    cooldownResetAndBuff.put(id, System.currentTimeMillis()-cooldownTime*1000);

                int secondsLeft = (int) ((cooldownResetAndBuff.get(id)/1000 + cooldownTime) - System.currentTimeMillis()/1000);
                if(secondsLeft <= 0){
                    cooldownResetAndBuff.put(id, System.currentTimeMillis());

                    p.removePotionEffect(PotionEffectType.SLOW);
                    p.removePotionEffect(PotionEffectType.CONFUSION);
                    p.removePotionEffect(PotionEffectType.BLINDNESS);
                    p.removePotionEffect(PotionEffectType.WEAKNESS);
                    p.removePotionEffect(PotionEffectType.POISON);
                    p.removePotionEffect(PotionEffectType.WITHER);
                    p.removePotionEffect(PotionEffectType.BAD_OMEN);
                    p.removePotionEffect(PotionEffectType.LEVITATION);
                    p.removePotionEffect(PotionEffectType.UNLUCK);
                    p.removePotionEffect(PotionEffectType.GLOWING);

                    p.addPotionEffect(PotionEffectType.REGENERATION.createEffect(duration*20, 1));
                    p.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(duration*20, 1));
                    p.addPotionEffect(PotionEffectType.SPEED.createEffect(duration*20, 1));
                    p.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(duration*20, 0));

                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            ActionBar.send(p, ChatColor.GREEN + "Очищение перезарядилось!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime*20);
                }
            }
        }
    }

}
