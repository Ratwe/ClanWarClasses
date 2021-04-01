package skills;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import spell.Spell;

public class skillAlmostDead extends Spell {

    @Override
    public void castSpell(Player player, int cooldown, double damage) {
        try {
            if (cdManager.isOver()) {
                double MaxHealth = player.getHealthScale();
                player.setHealth(MaxHealth / 2 + damage);
                cdManager.startCooldown(cooldown);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + "AlmostDead перезаряжается ещё " + cdManager.getRest() + " секунд."));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
