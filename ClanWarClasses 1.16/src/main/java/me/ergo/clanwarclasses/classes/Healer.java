package me.ergo.clanwarclasses.classes;

import me.ergo.clanwarclasses.tools.ActionBar;
import me.ergo.clanwarclasses.ClanWarClasses;
import me.ergo.clanwarclasses.exp.ExpPlayer;
import me.ergo.clanwarclasses.exp.ExpPlayerMap;
import me.ergo.clanwarclasses.tools.ClanTools;
import me.ergo.clanwarclasses.tools.Cooldown;
import me.ergo.clanwarclasses.tools.PEX;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import ru.tehkode.permissions.*;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.*;
import static java.lang.Math.min;

public class Healer implements Listener {
    // регистрация класса
    private final ClanWarClasses plugin;
    public Healer(ClanWarClasses plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /** Увеличенный урон по нежити **/
    @EventHandler
    public void increaseUndeadDamage (EntityDamageByEntityEvent event){
        if(event.getDamager().getType() == EntityType.PLAYER){
            Player p = (Player) event.getDamager();

            if(PEX.inGroup(p, "healer")){
                List<EntityType> undeads = new ArrayList<>(Arrays.asList(EntityType.SKELETON, EntityType.SNOWMAN, EntityType.WITHER_SKELETON, EntityType.WITCH, EntityType.ZOMBIE, EntityType.HUSK,
                        EntityType.ZOMBIFIED_PIGLIN, EntityType.ZOMBIE_VILLAGER, EntityType.DROWNED, EntityType.ZOMBIE_HORSE, EntityType.SKELETON_HORSE, EntityType.PHANTOM, EntityType.ZOGLIN));
                if(undeads.contains(event.getEntity().getType()))
                    event.setDamage(event.getDamage() * 1.3);
            }
        }
    }

    /** Массовый хил **/
    HashMap<UUID, Long> cooldownMassiveHeal = new HashMap<>();

    @EventHandler
    public void massiveHeal(PlayerItemConsumeEvent event) {
        int cooldownTime = 12;
        int unlock_lvl = 10;

        if (event.getItem().getType() == Material.POTION) {

            Player player = event.getPlayer();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
            PotionMeta potionMeta = (PotionMeta) event.getItem().getItemMeta();

            if (potionMeta.getBasePotionData().getType().equals(PotionType.INSTANT_HEAL)) {
                if (user.inGroup("healer")) {

                    HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
                    if (!map.containsKey(id))
                        map.put(id, new ExpPlayer());

                    int lvl = map.get(id).lvl;
                    int r = 10; // радиус

                    List<Entity> list = player.getNearbyEntities(r, r, r);

                    if (map.containsKey(id) && lvl >= unlock_lvl) {
                        if (!cooldownMassiveHeal.containsKey(id)) // игрока нет в мапе кулдауна
                            cooldownMassiveHeal.put(id, System.currentTimeMillis() - cooldownTime*1000);

                        int secondsLeft = (int) (((cooldownMassiveHeal.get(player.getUniqueId()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся презарядка
                        if (secondsLeft < 0) {
                            for (Entity entity : list) { // чекаем всех сущностей в радиусе x
                                if (entity.getType() == EntityType.PLAYER) {
                                    Player curPlayer = (Player) entity;
                                    if (ClanTools.sameClan(player, curPlayer)) { // если оба в одном клане
                                        curPlayer.setHealth(min(curPlayer.getHealthScale(), curPlayer.getHealth() + 4));
                                        ActionBar.send(player, ChatColor.YELLOW + "Вы восстановили 4 здоровья " + curPlayer.getName() + '.');
                                    }
                                }
                            }

                            new BukkitRunnable(){
                                public void run(){
                                    ActionBar.send(player, ChatColor.GREEN + "Массовое восстановление перезарядилось!");
                                }
                            }.runTaskLater(this.plugin, cooldownTime*20);
                        }
                    }
                }
            }
        }
    }

    /** Массовая регенерация **/
    HashMap<UUID, Long> cooldownMassiveRegen = new HashMap<>();

    @EventHandler
    public void massiveRegen(PlayerItemConsumeEvent event) {
        int cooldownTime = 15;
        int unlock_lvl = 10;
        int duration = 10;

        if (event.getItem().getType() == Material.POTION) {

            Player player = event.getPlayer();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);
            PotionMeta potionMeta = (PotionMeta) event.getItem().getItemMeta();

            if (potionMeta.getBasePotionData().getType().equals(PotionType.REGEN)) {
                if (user.inGroup("healer")) {

                    HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
                    if (!map.containsKey(id))
                        map.put(id, new ExpPlayer());

                    int lvl = map.get(id).lvl;
                    int r = 10; // радиус

                    List<Entity> list = player.getNearbyEntities(r, r, r);

                    if (map.containsKey(id) && lvl >= unlock_lvl) {
                        if (!cooldownMassiveRegen.containsKey(id)) // игрока нет в мапе кулдауна
                            cooldownMassiveRegen.put(id, System.currentTimeMillis() - cooldownTime*1000);

                        int secondsLeft = (int) (((cooldownMassiveRegen.get(player.getUniqueId()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся презарядка
                        if (secondsLeft < 0) {
                            for (Entity entity : list) { // чекаем всех сущностей в радиусе x
                                if (entity.getType() == EntityType.PLAYER) {
                                    Player curPlayer = (Player) entity;
                                    if (ClanTools.sameClan(player, curPlayer)) { // если оба в одном клане
                                        curPlayer.addPotionEffect(PotionEffectType.REGENERATION.createEffect(duration*20, 0));
                                        ActionBar.send(player, ChatColor.YELLOW + "Наложили эффект регенерации на игрока" + curPlayer.getName() + '.');
                                    }
                                }
                            }

                            new BukkitRunnable(){
                                public void run(){
                                    ActionBar.send(player, ChatColor.GREEN + "Массовая регенерация перезарядилась!");
                                }
                            }.runTaskLater(this.plugin, cooldownTime*20);
                        }
                    }
                }
            }
        }
    }

    /*@EventHandler
    public void drinkPotion(PlayerItemConsumeEvent event) {
        int cooldownTime = 12;
        int unlock_lvl = 10;

        if (event.getItem().getType() == Material.POTION) {

            Player player = event.getPlayer();
            String name = player.getName();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);
            PotionMeta potionMeta;

            boolean regen = false;

            potionMeta = (PotionMeta) event.getItem().getItemMeta();
            assert potionMeta != null;
            if (potionMeta.getBasePotionData().getType().equals(PotionType.REGEN))
                regen = true;
            else if (!potionMeta.getBasePotionData().getType().equals(PotionType.INSTANT_HEAL))
                return;

            if (user.inGroup("healer")) {
                HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
                if (!map.containsKey(id))
                    map.put(id, new ExpPlayer());

                int lvl = map.get(id).lvl;
                int r = 10; // радиус
                int count = 4; // сколько целей

                List<Entity> list = player.getNearbyEntities(r, r, r);
                HashMap<Player, Double> lowHP = new HashMap<>();

                if (map.containsKey(id) && lvl >= unlock_lvl) {

                    if (cooldownPotion.containsKey(player.getUniqueId())) { // игрок уже есть в мапе кулдауна
                        int secondsLeft = (int) (((cooldownPotion.get(player.getUniqueId()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся презарядка
                        if (secondsLeft < 0) {
                            for (Entity entity : list) { // чекаем всех сущностей в радиусе x
                                if (entity.getType() == EntityType.PLAYER) {
                                    Player curPlayer = (Player) entity;
                                    if (ClanTools.sameClan(player, curPlayer)) // если оба в одном клане
                                        lowHP.put(curPlayer, curPlayer.getHealth());

                                }

                                // сортировка по значению
                                Set<Map.Entry<Player, Double>> entrySet = lowHP.entrySet();
                                List<Map.Entry<Player, Double>> listHP = new ArrayList<>(entrySet);
                                listHP.sort(new Comparator<Map.Entry<Player, Double>>() {
                                    @Override
                                    public int compare(Map.Entry<Player, Double> o1, Map.Entry<Player, Double> o2) {
                                        return o1.getValue().compareTo(o2.getValue());
                                    }
                                });
                                // теперь listHP отсортирован

                                for (int i = 0; i < min(listHP.size(), count); i++) {
                                    Player p = listHP.get(i).getKey();
                                    if (regen) { // если выпито зелье регенерации
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 1));
                                        ActionBar.send(player, ChatColor.YELLOW + "Вы наложили игроку " + p.getName() + " эффект регенерации.");
                                    } else { //мгновенного восстановления
                                        p.setHealth(min(p.getHealthScale(), p.getHealth() + 4));
                                        ActionBar.send(player, ChatColor.YELLOW + "Вы восстановили 4 здоровья " + p.getName() + '.');
                                    }
                                    count--;
                                }
                            }
                        } else {
                            cooldownPotion.put(player.getUniqueId(), System.currentTimeMillis()); //обновляем кулдаун

                            for (Entity entity : list) { // чекаем всех сущностей в радиусе x
                                if (entity.getType() == EntityType.PLAYER) {
                                    Player curPlayer = (Player) entity;
                                    if (ClanTools.sameClan(player, curPlayer))
                                        lowHP.put(curPlayer, curPlayer.getHealth());
                                }
                            }
                            // сортировка по значению
                            Set<Map.Entry<Player, Double>> entrySet = lowHP.entrySet();
                            List<Map.Entry<Player, Double>> listHP = new ArrayList<>(entrySet);
                            listHP.sort(new Comparator<Map.Entry<Player, Double>>() {
                                @Override
                                public int compare(Map.Entry<Player, Double> o1, Map.Entry<Player, Double> o2) {
                                    return o1.getValue().compareTo(o2.getValue());
                                }
                            });
                            // теперь listHP отсортирован

                            for (int i = 0; i < min(listHP.size(), count); i++) {
                                Player p = listHP.get(i).getKey();
                                if (regen) { // если выпито зелье регенерации
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10*20, 1));
                                    player.sendMessage(ChatColor.YELLOW + "Вы наложили игроку " + p.getName() + " эффект регенерации");
                                } else { //мгновенного восстановления
                                    p.setHealth(min(p.getHealthScale(), p.getHealth() + 4));
                                    player.sendMessage(ChatColor.YELLOW + "Вы восстановили 4 здоровья " + p.getName());
                                }
                                count--;
                            }
                        }
                    }
                }
            }
        }
    }*/


    /** увеличение регенерации от зелий/утоления голода/еды **/
    @EventHandler
    public void regainHealth(EntityRegainHealthEvent event){
        int unlock_lvl = 20;
        Entity entity = event.getEntity();
        if(entity.getType() == EntityType.PLAYER){

            Player player = (Player) event.getEntity();
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            if(user.inGroup("healer")) {
                EntityRegainHealthEvent.RegainReason reason = event.getRegainReason();
                if (reason == EntityRegainHealthEvent.RegainReason.SATIATED || reason == EntityRegainHealthEvent.RegainReason.MAGIC
                        || reason == EntityRegainHealthEvent.RegainReason.MAGIC_REGEN || reason == EntityRegainHealthEvent.RegainReason.EATING) {

                    HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
                    if (map.containsKey(id) && map.get(id).lvl > unlock_lvl) {
                        event.setAmount(event.getAmount() * 1.2);
                    }
                }
            }
        }
    }

    /** Отталкивание **/
    HashMap<UUID, Long> cooldownRejection = new HashMap<>();

    @EventHandler
    public void rejection (PlayerInteractEvent event){
        int cooldownTime = 10;
        int r = 5;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if(PEX.inGroup(p, "healer")) {
            if (event.getMaterial() == Material.SUGAR) {
                if (!cooldownRejection.containsKey(id))
                    cooldownRejection.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                int secondsLeft = Cooldown.SecLeft(cooldownRejection.get(id), cooldownTime);
                if (secondsLeft <= 0) {
                    cooldownRejection.put(id, System.currentTimeMillis());

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
                        vector.multiply(new Vector(2, 2.4, 2));

                        e.setVelocity(vector);
                    }

                    new BukkitRunnable() {
                        public void run() {
                            ActionBar.send(p, ChatColor.GREEN + "Отталкивание перезарядилось!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime * 20);
                }
            }
        }
    }

    /** Боевой клич - скорость, огнестойкость, сила **/
    HashMap<UUID, Long> cooldownBattleCry = new HashMap<>();

    @EventHandler
    public void battleCry(PlayerInteractEvent event){
        int cooldownTime = 12;
        int duration = 10;
        int r = 10;
        Player p = event.getPlayer();
        UUID id = p.getUniqueId();

        if(PEX.inGroup(p, "healer")){
            if(event.getMaterial() == Material.WOODEN_SWORD){
                if(!cooldownBattleCry.containsKey(id))
                    cooldownBattleCry.put(id, System.currentTimeMillis() - cooldownTime*1000);

                int secondsLeft = Cooldown.SecLeft(cooldownBattleCry.get(id), cooldownTime);
                if(secondsLeft <= 0){
                    cooldownBattleCry.put(id, System.currentTimeMillis());
                    p.addPotionEffect(PotionEffectType.SPEED.createEffect(duration*20, 0));
                    p.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(duration*20, 0));
                    p.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(duration*20, 0));

                    List<Entity> list = p.getNearbyEntities(r, r, r);
                    for (Entity e : list)
                        if (e.getType() == EntityType.PLAYER)
                            if (ClanTools.sameClan((Player) e, p)){
                                ((Player) e).addPotionEffect(PotionEffectType.SPEED.createEffect(duration*20, 0));
                                ((Player) e).addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(duration*20, 0));
                                ((Player) e).addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(duration*20, 0));
                            }

                    new BukkitRunnable() {
                        public void run() {
                            ActionBar.send(p, ChatColor.GREEN + "Боевой клич перезарядился!");
                        }
                    }.runTaskLater(this.plugin, cooldownTime*20);
                }
            }
        }
    }

    /** Воля к жизни **/
    HashMap<UUID, Long> cooldownAlmostDead = new HashMap<>();

    @EventHandler
    public void almostDead(EntityDamageEvent event){
        int cooldownTime = 10;

        Entity entity = event.getEntity();
        if(event.getEntityType() == EntityType.PLAYER) {//если дамаг получил игрок

            Player player = (Player) entity;
            UUID id = player.getUniqueId();
            PermissionUser user = PermissionsEx.getPermissionManager().getUser(id);

            double damage = event.getFinalDamage();
            double curHealth = player.getHealth();
            if (damage >= curHealth) {
                if (user.inGroup("healer")) {
                    if (!cooldownAlmostDead.containsKey(player.getUniqueId()))
                        cooldownAlmostDead.put(id, System.currentTimeMillis() - cooldownTime * 1000);

                    int secondsLeft = (int) (((cooldownAlmostDead.get(player.getUniqueId()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся презарядка
                    if (secondsLeft <= 0) {
                        //восполняем здоровье до половины
                        double maxHealth = player.getHealthScale();
                        player.setHealth(maxHealth / 2 + damage);
                        cooldownAlmostDead.put(player.getUniqueId(), System.currentTimeMillis()); //обновляем кулдаун

                        new BukkitRunnable() { //сообщение о конце перезарядки
                            @Override
                            public void run() {
                                ActionBar.send(player, ChatColor.GREEN + "Воля к жизни перезарядилась!");
                            }
                        }.runTaskLater(this.plugin, cooldownTime * 20L); //через cooldownTime секунд

                    }
                }
            }
        }
    }
}
