package me.ergo.clanwarclasses.classes;

import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.tools.ActionBar;
import me.ergo.clanwarclasses.tools.Move;
import me.ergo.clanwarclasses.tools.PEX;
import me.ergo.clanwarclasses.tools.PlayerJumpEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.UUID;


public class Archer implements Listener {
    // регистрация класса
    ClanWarClasses plugin;
    public Archer(ClanWarClasses plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /** Увеличение скорости стрелы из лука **/
    @EventHandler
    public void arrowSpeed(ProjectileLaunchEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.ARROW) {
            Arrow arrow = (Arrow) entity;
            if (arrow.getShooter() instanceof Player) {
                Player player = (Player) arrow.getShooter();
                UUID id = player.getUniqueId();

                if (PEX.inGroup(player, "archer")) {
                    arrow.setVelocity(arrow.getVelocity().multiply(2)); // ускорение стрелы в 2 раза
                    //entity.setGravity(false); // можно гравитацию убрать (что если запустить в небо...)
                }
            }
        }
    }

    /** Стрела отравления **/
    final HashMap<UUID, Long> cooldownPoisonArrow = new HashMap<>();

    @EventHandler
    public void poisonArrow(PlayerInteractEvent event) {
        // в руках не лук, а зачаренная стрела
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        int cooldownTime = 10;
        int duration = 3;

        if (event.getMaterial() == Material.TIPPED_ARROW) {
            if (PEX.inGroup(player, "archer")) {

                ItemStack item = event.getItem();
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                if (meta.getBasePotionData().getType().getEffectType()==PotionEffectType.POISON) {

                    if (!cooldownPoisonArrow.containsKey(id))
                        cooldownPoisonArrow.put(id, System.currentTimeMillis() - cooldownTime * 1000);
                    int secondsLeft = (int) (((cooldownPoisonArrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка

                    if (secondsLeft <= 0) {
                        Location loc = player.getEyeLocation(); // локация, куда смотрит игрок
                        Arrow arrow = player.launchProjectile(Arrow.class); // спавним новую стрелу
                        arrow.setVelocity(loc.getDirection().multiply(2)); // направляем её в сторону взгляда и увеличиваем скорость
                        arrow.addCustomEffect(PotionEffectType.POISON.createEffect(duration * 20, 2), true); // чарим на яд

                        cooldownPoisonArrow.put(id, System.currentTimeMillis()); //обновляем кулдаун

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActionBar.send(player, ChatColor.GREEN + "Стрела отравления перезарядилась!");
                            }
                        }.runTaskLater(this.plugin, cooldownTime * 20L); // через cooldownTime секунд
                    }
                }
            }
        }
    }

    /** Стрела медлительности **/
    final HashMap<UUID, Long> cooldownSlownessArrow = new HashMap<>();

    @EventHandler
    public void slownessArrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        long cooldownTime = 10;
        long duration = 5;

        if (event.getMaterial() == Material.TIPPED_ARROW) {
            if (PEX.inGroup(player, "archer")) {

                ItemStack item = event.getItem();
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                if (meta.getBasePotionData().getType().getEffectType()==PotionEffectType.SLOW) {

                    if (!cooldownSlownessArrow.containsKey(id))
                        cooldownSlownessArrow.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                    long secondsLeft = (((cooldownSlownessArrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка

                    if (secondsLeft <= 0) {
                        Location loc = player.getEyeLocation(); // локация, куда смотрит игрок
                        Arrow arrow = player.launchProjectile(Arrow.class); // спавним новую стрелу
                        arrow.setVelocity(loc.getDirection().multiply(2)); // направляем её в сторону взгляда и увеличиваем скорость
                        arrow.addCustomEffect(PotionEffectType.SLOW.createEffect((int) (duration * 20), 2), true); // чарим на яд

                        cooldownSlownessArrow.put(id, System.currentTimeMillis()); //обновляем кулдаун

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActionBar.send(player, ChatColor.GREEN + "Стрела медлительности перезарядилась!");
                            }
                        }.runTaskLater(this.plugin, cooldownTime * 20L); // через cooldownTime секунд
                    }
                }
            }
        }
    }


    /** Стрела прыгучести **/
    final HashMap<UUID, Long> cooldownJumpArrow = new HashMap<>();

    @EventHandler
    public void jumpArrow(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        int cooldownTime = 10;
        int duration = 10;

        if (event.getMaterial() == Material.TIPPED_ARROW) {
            if (PEX.inGroup(player, "archer")) {

                ItemStack item = event.getItem();
                PotionMeta meta = (PotionMeta) item.getItemMeta();
                if (meta.getBasePotionData().getType().getEffectType()==PotionEffectType.JUMP) {

                    if (!cooldownJumpArrow.containsKey(id))
                        cooldownJumpArrow.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                    int secondsLeft = (int) (((cooldownJumpArrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                    if (secondsLeft <= 0) {
                        Location loc = player.getEyeLocation(); // локация, куда смотрит игрок
                        Arrow arrow = player.launchProjectile(Arrow.class); // спавним новую стрелу
                        arrow.setVelocity(loc.getDirection().multiply(0.5)); // направляем её в сторону взгляда и УМЕНЬШАЕМ скорость (чтобы легко было в себя попасть)
                        arrow.addCustomEffect(PotionEffectType.JUMP.createEffect(duration * 20, 2), true); // чарим

                        cooldownJumpArrow.put(id, System.currentTimeMillis()); //обновляем кулдаун

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActionBar.send(player, ChatColor.GREEN + "Стрела прыгучести перезарядилась!");
                            }
                        }.runTaskLater(this.plugin, cooldownTime * 20L); // через cooldownTime секунд
                    }
                }
            }
        }
    }

    /** Призрачная стрела **/

    final HashMap<UUID, Long> cooldownSpectralArrow = new HashMap<>();

    @EventHandler
    public void spectralArrow(PlayerInteractEvent event) {
        int cooldownTime = 10;
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        if (event.getMaterial() == Material.SPECTRAL_ARROW) {
            if (PEX.inGroup(player, "archer")) {

                if (!cooldownSpectralArrow.containsKey(id))
                    cooldownSpectralArrow.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                int secondsLeft = (int) (((cooldownSpectralArrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000));
                if (secondsLeft <= 0) {
                    Location loc = player.getEyeLocation();
                    SpectralArrow arrow = player.launchProjectile(SpectralArrow.class);
                    arrow.setVelocity(loc.getDirection().multiply(3));

                    cooldownSpectralArrow.put(id, System.currentTimeMillis());

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ActionBar.send(player, ChatColor.GREEN + "Спектральная стрела перезарядилась!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime * 20L);
                }

            }
        }
    }


    // при подборе зачарованная стрела сразу удаляется
    @EventHandler
    public void pickUpTippedArrow(PlayerPickupArrowEvent event){
        ItemStack item = event.getItem().getItemStack();
        if(item.getType() == Material.TIPPED_ARROW || item.getType() == Material.SPECTRAL_ARROW) {

            Player shooter = (Player) event.getArrow().getShooter();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(shooter.getName());
            if(user.inGroup("archer")) { // никакую чареную (и спектральную) стрелу, которую выстрелил лучник, нельзя подобрать
                event.getArrow().remove();
                event.setCancelled(true);
            }
        }
    }


    /** Отскок **/
    final HashMap<UUID, Long> cooldownRebound = new HashMap<>();
    int cooldownTimeRebound = 5;

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player.getName());
        if(user.inGroup("archer") && player.getGameMode() == GameMode.SURVIVAL) {
            if (!cooldownRebound.containsKey(id))
                cooldownRebound.put(player.getUniqueId(), System.currentTimeMillis()-cooldownTimeRebound* 1000L);

            int secondsLeft = (int) (((cooldownRebound.get(id) / 1000) + cooldownTimeRebound) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
            if (secondsLeft <= 0) {
                cooldownRebound.put(player.getUniqueId(), System.currentTimeMillis()); //обновляем кулдаун

                Location loc = player.getLocation().clone();
                Vector jump = loc.getDirection();
                jump.multiply(new Vector(-0.7, -0.8, -0.7));
                player.setVelocity(jump/*.multiply(-0.7)*/); // прыгаем назад
                player.setAllowFlight(false);

                new BukkitRunnable() { //сообщение о конце перезарядки
                    @Override
                    public void run() {
                        ActionBar.send(player, ChatColor.GREEN + "Отскок перезарядился!");
                        player.setAllowFlight(true);
                    }
                }.runTaskLater(this.plugin, cooldownTimeRebound * 20L); //через cooldownTime секунд
            }


            player.setFlying(false);
            event.setCancelled(true);
        }
    }

    /** Когда игрок прыгает - ему даётся флай для отскока, а потом отбирается **/
    @EventHandler
    public void rebound(PlayerJumpEvent event){
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player.getName());

        if((user.inGroup("archer") || user.inGroup("warrior")) && player.getGameMode() == GameMode.SURVIVAL){
            player.setAllowFlight(true);
            new BukkitRunnable() { // отбираем флай
                @Override
                public void run() {
                    player.setAllowFlight(false);
                }
            }.runTaskLater(this.plugin, 40L);
        }
    }


    /** Взлёт **/
    HashMap<UUID, Long> cooldownAscent = new HashMap<>();

    @EventHandler
    public void ascent(PlayerInteractEvent event){
        int cooldownTime = 20;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if(PEX.inGroup(p, "archer"))
            if(event.getMaterial() == Material.FEATHER){
                if(!cooldownAscent.containsKey(id))
                    cooldownAscent.put(id, System.currentTimeMillis()-cooldownTime*1000);

                int secondsLeft = (int) ((cooldownAscent.get(id)/1000 + cooldownTime) - System.currentTimeMillis()/1000);
                if(secondsLeft <= 0){
                    Location loc = p.getLocation();
                    loc.setY(loc.getY()+30);
                    p.teleport(loc);
                    p.addPotionEffect(PotionEffectType.SLOW_FALLING.createEffect(6*20, 0));

                    cooldownAscent.put(id, System.currentTimeMillis());
                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            ActionBar.send(p, ChatColor.GREEN + "Взлёт перезарядился!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime*20);
                }
            }

    }

    /* Извержение стрел **/
}
