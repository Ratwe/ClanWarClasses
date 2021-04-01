package me.ergo.clanwarclasses.exp;

import static java.lang.Math.pow;

public class ExpPlayer {
    public int lvl = 0;
    public int exp = 0;
    public double new_lvl_exp = pow(1.1, lvl) * 10;
}
