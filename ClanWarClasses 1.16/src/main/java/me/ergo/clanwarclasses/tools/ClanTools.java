package me.ergo.clanwarclasses.tools;

import com.youtube.hempfest.clans.util.construct.ClanUtil;
import org.bukkit.entity.Player;

public class ClanTools {
    public static boolean sameClan(Player player, Player curPlayer) {
        ClanUtil clanUtil = new ClanUtil();
        return clanUtil.getClan(curPlayer) != null && clanUtil.getClan(player) != null && clanUtil.getClan(curPlayer).equals(clanUtil.getClan(player));
    }
}
