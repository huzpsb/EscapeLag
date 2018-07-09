package com.mcml.space.optimize;

import org.bukkit.Bukkit;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.Ticker;

public class TickSleep {
    public static volatile long sleepMillis = 20;
    
    public static void init() {
        if (ConfigOptimize.TPSSleepNoOneFreezeenable || !ConfigOptimize.TPSSleepSleepMode.equalsIgnoreCase("NoUse"))
            Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable() {
                public void run() {
                    try {
                        if (ConfigOptimize.TPSSleepNoOneFreezeenable && PlayerList.isEmpty()) {
                            Thread.sleep(TickSleep.getTargetSleepTime(5));
                        }
                        
                        if (ConfigOptimize.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep")) {
                            Thread.sleep(TickSleep.getTargetSleepTime(sleepMillis));
                        } else if (ConfigOptimize.TPSSleepSleepMode.matches("[0-9]+")) {
                            Thread.sleep(TickSleep.getTargetSleepTime(Integer.parseInt(ConfigOptimize.TPSSleepSleepMode)));
                        }
                    } catch (InterruptedException ex) {}
                }
            }, 1L, 1L);
        
        if (ConfigOptimize.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep"))
            Bukkit.getScheduler().runTaskTimerAsynchronously(EscapeLag.plugin, new Runnable() {
                public void run() {
                    if (sleepMillis < 20) {
                        if (sleepMillis - 1 < Ticker.getRealTimeTPS()) {
                            sleepMillis++;
                        } else {
                            sleepMillis = Ticker.getRealTimeTPS();
                        }
                    } else {
                        if (Ticker.getRecentTPS() <= 18) sleepMillis = Ticker.getRealTimeTPS();
                    }
                }
            }, 40L, 40L);
    }
    
    static long getTargetSleepTime(long raw) {
        return (50 - ((5 * raw) / 2));
    }
}
