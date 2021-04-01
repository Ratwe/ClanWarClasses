package cooldown;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by TheL0w3R on 28/06/2015.
 * All Rights Reserved.
 */
public class CooldownManager {

    private int rest;
    private final Timer timer = new Timer();

    private TimerTask timerTask;

    public CooldownManager() {
        rest = 0;
    }

    public void startCooldown(int cooldown) {
        rest = cooldown;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(!(rest == 0)) {
                    rest--;
                } else {
                    this.cancel();
                }
            }
        }, 0, 1000);
    }

    public int getRest() {
        return rest;
    }

    public boolean isOver() {
        return rest == 0;
    }

}