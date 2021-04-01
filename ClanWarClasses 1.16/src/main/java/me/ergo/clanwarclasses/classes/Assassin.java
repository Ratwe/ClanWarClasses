package me.ergo.clanwarclasses.classes;

import com.youtube.hempfest.clans.util.events.ClaimResidentEvent;
import com.youtube.hempfest.clans.util.events.WildernessInhabitantEvent;
import me.ergo.clanwarclasses.tools.ActionBar;
import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.tools.ClanTools;
import me.ergo.clanwarclasses.tools.RandomNum;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;

import static java.lang.Math.*;

public class Assassin implements Listener {
    ClanWarClasses plugin;
    public Assassin(ClanWarClasses plugin){
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /** Засада **/
    HashMap<UUID, Location> map = new HashMap<>();
    HashMap<UUID, Long> cooldownBurrow = new HashMap<>();
    HashMap<UUID, Boolean> burrowAvailable = new HashMap<>();

    @EventHandler
    public void burrowAvailable(ClaimResidentEvent event){ // стапает на приваченнуб территорию
        Player player = event.getResident().getPlayer();
        UUID id = player.getUniqueId();

        List<String> members = Arrays.asList(event.getClaim().getClan().getMembers());
        if (members.contains(player.getName())) // если находится в клане, которому принадлежит этот чанк
            burrowAvailable.put(id, true); // то можно засаду делать
        else
            burrowAvailable.put(id, false);
    }

    @EventHandler
    public void burrowAvailableWildness(WildernessInhabitantEvent event){
        burrowAvailable.put(event.getPlayer().getUniqueId(), true); // если на свободных землях - можно засаду
    }

    HashMap<UUID, Long> antiDoubleInteract = new HashMap<>();

    @EventHandler
    public void burrowOn(PlayerInteractEvent event){
        long cooldownTime = 7;
        int duration = 60*60;
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player.getName());

        if(user.inGroup("assassin")) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (player.getInventory().getItemInMainHand().getType() == Material.WOODEN_SHOVEL) {

                    if (!cooldownBurrow.containsKey(id))
                        cooldownBurrow.put(id, System.currentTimeMillis()-cooldownTime*1000);
                    if (!antiDoubleInteract.containsKey(id))
                        antiDoubleInteract.put(id, System.currentTimeMillis()-100);

                    int secondsLeft = (int) (((cooldownBurrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                    int millsAfter = (int) (System.currentTimeMillis() - antiDoubleInteract.get(id)); // сколько миллисек прошло с прошлого клика
                    if (secondsLeft <= 0 && millsAfter >= 200) { // эта проверка с millsAfter нужна чтобы второй раз ивент не регался (нажатие воспринимает за два ивента с мельчайшей задержкой в +-5 мс)
                        Block block = event.getClickedBlock();
                        Location loc = map.get(id);
                        Location ploc = player.getLocation();
                        loc = block.getLocation(); // берём координаты блока, на который кликнули
                        loc.setYaw(ploc.getYaw());
                        loc.setPitch(ploc.getPitch());

                        if (loc.getX()==floor(ploc.getX()) && loc.getY() == round(ploc.getY()-1) && loc.getZ()==floor(ploc.getZ()) ) { // если блок под игроком
                            if (burrowAvailable.get(id)) {
                                // понижать высоту на единицу нет смысла - это сделало округление
                                loc.setX(loc.getX() + 0.5);
                                loc.setZ(loc.getZ() + 0.5);

                                player.teleport(loc); // телепортируем игрока ногами в блок
                                player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(duration*20, 1));
                                event.setCancelled(true); // чтобы не было выкопанного блока - палево

                                map.put(id, loc);
                                cooldownBurrow.put(id, System.currentTimeMillis());
                                cooldownBurrow.put(id, System.currentTimeMillis()); //обновляем кулдаун
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        ActionBar.send(player, ChatColor.GREEN + "Засада перезарядилась!");
                                    }
                                }.runTaskLater(this.plugin, (cooldownTime * 20L)); // через cooldownTime секунд
                            }
                            else {
                                player.sendMessage(ChatColor.RED + "Вы не можете использовать это умение на чужой территории!");
                                event.setCancelled(true);
                            }

                            antiDoubleInteract.put(id, System.currentTimeMillis());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void burrowOff(PlayerMoveEvent event){
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        Location loc = player.getLocation();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(player.getName());

        if (map.containsKey(id)) {
            // движение мышкой тоже регает, поэтому проверяем именно координаты, где стоит игрок
            Location lastLoc = map.get(id);
            if (lastLoc.getX() != loc.getX() || lastLoc.getY() != loc.getY() || lastLoc.getZ() != loc.getZ()) { // поменялись ли у него координаты
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                map.remove(id);
            }
        }
    }

    HashMap<UUID, Long> cooldownBlindnessArrow = new HashMap<>();


    /** Стрела слепоты**/
    @EventHandler
    public void blindnessArrow(EntityDamageByEntityEvent event) {
        long cooldownTime = 20;
        long duration = 5;

        if (event.getEntity().getType().equals(EntityType.PLAYER)  && event.getDamager().getType().equals(EntityType.PLAYER)) { // если дамаг получил игрок
            Player player = (Player) event.getDamager();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.TIPPED_ARROW) // урон нанёсся стрелой
                if (user.inGroup("assassin")) {

                    if (!cooldownBlindnessArrow.containsKey(id))
                        cooldownBlindnessArrow.put(id, (System.currentTimeMillis()-cooldownTime*1000));

                    int secondsLeft = (int) (((cooldownBlindnessArrow.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                    if (secondsLeft <= 0) {
                        PotionMeta meta = (PotionMeta) item.getItemMeta();
                        if (meta.getBasePotionData().getType() == PotionType.WEAKNESS) { // стрела слабости
                            Player victim = (Player) event.getEntity();
                            victim.addPotionEffect(PotionEffectType.BLINDNESS.createEffect((int) (duration*20), 1));
                        }

                        cooldownBlindnessArrow.put(id, System.currentTimeMillis()); //обновляем кулдаун
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ActionBar.send(player, ChatColor.GREEN + "Стрела слепоты перезарядилась!");
                            }
                        }.runTaskLater(this.plugin, (cooldownTime * 20L)); // через cooldownTime секунд
                    }

                }
        }
    }

    /** Одиночка **/
    @EventHandler
    public void loner(EntityDamageByEntityEvent event){
        double r = 16;

        if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER) {
            Player p = (Player) event.getDamager();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(p.getName());

            if (user.inGroup("assassin")) {
                List<Entity> list = new ArrayList<>();
                list = p.getNearbyEntities(r, r, r);
                int count = 0;
                for (Entity entity : list) { // чекаем всех сущностей в радиусе x
                    if (entity.getType() == EntityType.PLAYER) {
                        Player curPlayer = (Player) entity;
                        if(!ClanTools.sameClan(curPlayer, p)) // если игрок не в одном клане с нападающим
                            count++;
                    }
                }

                if(count <= 1) // единственный человек НЕ союзник в округе - жертва
                    event.setDamage(event.getDamage() * 1.2);
            }
        }
    }


    /** Травма **/
    @EventHandler
    public void trauma(EntityDamageByEntityEvent event){
        int duration = 4;

        if(event.getDamager().getType() == EntityType.PLAYER && event.getEntity().getType() == EntityType.PLAYER) { // если дерутся два игрока
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            UUID id = damager.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            if(user.inGroup("assassin")){
                if(RandomNum.getRandomIntegerBetweenRange(1, 100) <= 5) // с шансом 5%
                    victim.addPotionEffect(PotionEffectType.SLOW.createEffect(duration * 20, 3)); // накладываем замедление на 45%
            }
        }
    }
}
