package me.ergo.clanwarclasses.exp;

import java.util.HashMap;
import java.util.UUID;

public class ExpPlayerMap {
    public HashMap<UUID, ExpPlayer> map = new HashMap<>();

    public HashMap<UUID, ExpPlayer> getMap() {
        map.put(MapPut.key, MapPut.value);
        return map;
    }
}
