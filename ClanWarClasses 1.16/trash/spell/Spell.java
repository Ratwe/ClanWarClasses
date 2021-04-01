package spell;

import cooldown.CooldownManager;
import org.bukkit.entity.Player;

/**
 * Created by TheL0w3R on 18/06/2015.
 * All Rights Reserved.
 */
public class Spell {

    public CooldownManager cdManager;

    public Spell() {
        cdManager = new CooldownManager();
    }

    public void castSpell(Player p, int cooldown, double damage) {

    }

}