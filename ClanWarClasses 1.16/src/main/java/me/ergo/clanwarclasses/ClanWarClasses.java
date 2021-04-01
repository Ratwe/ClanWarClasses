package me.ergo.clanwarclasses;

import me.ergo.clanwarclasses.classes.*;
import me.ergo.clanwarclasses.commands.*;
import me.ergo.clanwarclasses.exp.ExpManager;
import me.ergo.clanwarclasses.tools.ClickEvent;
import me.ergo.clanwarclasses.tools.EnderPerl;
import me.ergo.clanwarclasses.tools.PlayerJumpEvent;
import me.ergo.clanwarclasses.tools.RestorePotionEffects;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ClanWarClasses extends JavaPlugin {

    @Override
    public void onEnable() {
        new Healer(this);
        new Tank(this);
        new Warrior(this);
        new Archer(this);
        new Assassin(this);
        new ExpManager(this);
        new ClickEvent(this);
        new RestorePotionEffects(this);
        new EnderPerl(this);

        Objects.requireNonNull(getCommand("choose")).setExecutor(new Choose());
        Objects.requireNonNull(getCommand("stats")).setExecutor(new Stats());
        Objects.requireNonNull(getCommand("givexp")).setExecutor(new GiveXP());
        Objects.requireNonNull(getCommand("setlevel")).setExecutor(new SetLevel());
        Objects.requireNonNull(getCommand("skills")).setExecutor(new Skills());

        /** Привязываем TabCompleter к командам, чтобы выскакивали варианты аргументов при вводе **/
        Objects.requireNonNull(this.getCommand("choose")).setTabCompleter(new ChooseTabCompleter());
        Objects.requireNonNull(this.getCommand("skills")).setTabCompleter(new ChooseTabCompleter());

        PlayerJumpEvent.register(this);

        System.out.println("ClanWarClasses is enable");
        System.out.println("Author: Ergo");
    }

    @Override
    public void onDisable() {
        System.out.println("ClanWar Classes is disable");
    }
}
