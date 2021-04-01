package me.ergo.clanwarclasses.tools;

import org.bukkit.entity.Player;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PEX {
    public static boolean inGroup(Player p, String group){
        PermissionUser user = PermissionsEx.getPermissionManager().getUser(p);
        return user.inGroup(group);
    }
}
