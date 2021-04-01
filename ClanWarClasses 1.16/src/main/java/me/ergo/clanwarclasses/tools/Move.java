package me.ergo.clanwarclasses.tools;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Move {
    public static class Point {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class TriPoint {
        public final double x, y, z;

        public TriPoint(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static void moveSmooth(Player player, Location to, double diff) {
        double coveredX = 0, overallX = distFirstAxis(player.getLocation().getX(), to.getX()), xoff = overallX;
        double coveredY = 0, overallY = distFirstAxis(player.getLocation().getY(), to.getY()), yoff = overallY;
        double coveredZ = 0, overallZ = distFirstAxis(player.getLocation().getZ(), to.getZ()), zoff = overallZ;

        while(!(coveredX >= overallX & coveredY >= overallY & coveredZ >= overallZ)) {
            coveredX += diff;
            coveredY += diff;
            coveredZ += diff;

            player.teleport(new Location(player.getWorld(), coveredX + xoff, coveredY + yoff, coveredZ + zoff));
        }
    }

    public static double distFirstAxis(double x, double y) {
        return Math.max(x - y, y - x);
    }

    public static double distSecondAxis(Point x, Point y) {
        double xdiff = distFirstAxis(x.x, y.x);
        double ydiff = distFirstAxis(x.y, y.y);

        return Math.sqrt((xdiff * xdiff) + (ydiff * ydiff));
    }

    public static double distThridAxis(TriPoint x, TriPoint y) {
        double xd = distFirstAxis(x.x, y.x);
        double yd = distFirstAxis(x.y, y.y);
        double zd = distFirstAxis(x.z, y.z);

        return Math.sqrt(xd + yd + zd);
    }
}
