package me.ergo.clanwarclasses.commands;

import me.ergo.clanwarclasses.exp.ExpPlayer;
import me.ergo.clanwarclasses.exp.ExpPlayerMap;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class Choose implements CommandExecutor{

    public void removeEffects(Player player){
        HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
        UUID id = player.getUniqueId();

        if(!map.containsKey(id))
            map.put(id, new ExpPlayer());

        player.setHealthScale(20+map.get(id).lvl/20);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ARMOR)).setBaseValue(0);
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_SPEED)).setBaseValue(4); // возможен баг с Combat+
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE)).setBaseValue(0);
        player.setWalkSpeed((float) 0.2);

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        if(player.getGameMode() == GameMode.SURVIVAL)
            player.setAllowFlight(false);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.removePotionEffect(PotionEffectType.SLOW);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            String name = sender.getName();
            String Class = args[0];

            if(!Class.equals("healer") && !Class.equals("tank") && !Class.equals("warrior") && !Class.equals("archer") && !Class.equals("assassin") && !Class.equals("default")) {
                sender.sendMessage(ChatColor.RED + "Ты не можешь выбрать класс " + Class + ": его не существует");
                return true;
            }

            PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);

            String[] groups = user.getParentIdentifiers().toArray(new String[0]);
            String group = null;
            int i = 0;
            while(groups.length > i){
                if(!groups[i].equals("admin") && !groups[i].equals("moderator") && !groups[i].equals("helper") && !groups[i].equals("default")){
                    group = groups[i];
                    break;
                }
                i++;
            }

            if(group != null)
                sender.sendMessage(ChatColor.RED + "Ты не можешь сменить класс, т.к. уже выбрал " + group);
            else {
                if(user.inGroup("user"))
                    user.removeGroup("user");
                user.addGroup(Class);
                sender.sendMessage(ChatColor.GREEN + "Теперь ты - " + Class);

                Player player = ((Player) sender).getPlayer();
                removeEffects(player);

                if(Class.equals("healer")){
                    double maxHealth = player.getHealthScale();
                    player.setHealthScale(maxHealth-2); // уменьшаем жизни игрока на сердце
                }
                else if(Class.equals("tank")){
                    HashMap<UUID, ExpPlayer> map = new ExpPlayerMap().getMap();
                    UUID id = player.getUniqueId();
                    if(!map.containsKey(id))
                        map.put(id, new ExpPlayer());

                    player.setHealthScale(20+2*map.get(id).lvl/10);
                    player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(5); // кожанная броня даёт 7
                    player.addPotionEffect(PotionEffectType.SLOW.createEffect(60000, 0));

                    //double attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).getValue();
                    //player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed * 0.75); // по дефолту 4 (с CombatPlus 24)
                    player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.5); // отбрасывается всего на половину
                }
                else if(Class.equals("warrior")) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(3);
                }
                else if(Class.equals("archer")){
                    player.addPotionEffect(PotionEffectType.SPEED.createEffect(60000, 0));
                }
                else if(Class.equals("assassin")){
                    player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(60000, 0));
                }
            }

        }

        return true;
    }
}