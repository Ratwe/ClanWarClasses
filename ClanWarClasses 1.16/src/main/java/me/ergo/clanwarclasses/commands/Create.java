package me.ergo.clanwarclasses.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.util.HashMap;
import java.util.UUID;

public class Create implements CommandExecutor {

    HashMap<UUID, Long> cooldownPoison = new HashMap<>();
    HashMap<UUID, Long> cooldownSlowness = new HashMap<>();
    HashMap<UUID, Long> cooldownJump = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = sender.getName();
        Player player = Bukkit.getPlayer(name);
        UUID id = player.getUniqueId();
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(name);

        if(user.inGroup("archer")){
            ItemStack item = new ItemStack(Material.TIPPED_ARROW);
            PotionMeta meta = (PotionMeta) item.getItemMeta();

            if(args[0].equals("poison")) {
                int cooldownTime = 600000;
                if(!cooldownPoison.containsKey(id))
                    cooldownPoison.put(id, 0L); //обновляем кулдаун

                int secondsLeft = (int) (((cooldownPoison.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                if(secondsLeft <= 0) {
                    meta.setBasePotionData(new PotionData(PotionType.POISON));
                    player.sendMessage(ChatColor.YELLOW + "Вы получили стрелу яда!");
                    cooldownPoison.put(id, System.currentTimeMillis()); //обновляем кулдаун
                }
                else {
                    player.sendMessage(ChatColor.RED + "Перезарядка ещё " + secondsLeft / 3600 + " ч. " + secondsLeft % 3600 / 60 + " м. " + secondsLeft % 3600 % 60 + " с. ");
                    return true;
                }
            }

            else if(args[0].equals("slowness")) {
                int cooldownTime = 45;
                if(!cooldownSlowness.containsKey(id))
                    cooldownSlowness.put(id, 0L); //обновляем кулдаун

                int secondsLeft = (int) (((cooldownSlowness.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                if(secondsLeft <= 0) {
                    meta.setBasePotionData(new PotionData(PotionType.SLOWNESS));
                    player.sendMessage(ChatColor.YELLOW + "Вы получили стрелу замедления!");
                    cooldownSlowness.put(id, System.currentTimeMillis()); //обновляем кулдаун
                }
                else {
                    player.sendMessage(ChatColor.RED + "Перезарядка ещё " + secondsLeft / 3600 + " ч. " + secondsLeft % 3600 / 60 + " м. " + secondsLeft % 3600 % 60 + " с. ");
                    return true;
                }
            }

            else if(args[0].equals("jump")) {
                int cooldownTime = 30;
                if(!cooldownJump.containsKey(id))
                    cooldownJump.put(id, 0L); //обновляем кулдаун

                int secondsLeft = (int) (((cooldownJump.get(id) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000)); //оставшаяся перезарядка
                if(secondsLeft <= 0) {
                    meta.setBasePotionData(new PotionData(PotionType.JUMP));
                    player.sendMessage(ChatColor.YELLOW + "Вы получили стрелу прыжка!");
                    cooldownJump.put(id, System.currentTimeMillis()); //обновляем кулдаун
                }
                else {
                    player.sendMessage(ChatColor.RED + "Перезарядка ещё " + secondsLeft / 3600 + " ч. " + secondsLeft % 3600 / 60 + " м. " + secondsLeft % 3600 % 60 + " с. ");
                    return true;
                }
            }

            else {
                player.sendMessage(ChatColor.RED + "Вы должны выбрать <poison> или <slowness> или <jump>.");
                return true;
            }

            item.setItemMeta(meta);
            player.getInventory().addItem(item);
        }
        else
            player.sendMessage(ChatColor.RED + "Вы должны быть лучником, чтобы использовать эту команду.");

        return true;
    }

}
