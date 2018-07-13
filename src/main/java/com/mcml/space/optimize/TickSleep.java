package com.mcml.space.optimize;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Optimizations;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.Ticker;

public class TickSleep {
    private static volatile long sleepMillis = 20;
    
    public static void init(Plugin plugin) {
        if (Optimizations.TPSSleepNoOneFreezeenable || !Optimizations.TPSSleepSleepMode.equalsIgnoreCase("NoUse"))
            Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                public void run() {
                    try {
                        if (Optimizations.TPSSleepNoOneFreezeenable && PlayerList.isEmpty()) {
                            Thread.sleep(TickSleep.getTargetSleepTime(5));
                        }
                        
                        if (Optimizations.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep")) {
                            Thread.sleep(TickSleep.getTargetSleepTime(sleepMillis));
                        } else if (Optimizations.TPSSleepSleepMode.matches("[0-9]+")) {
                            Thread.sleep(TickSleep.getTargetSleepTime(Integer.parseInt(Optimizations.TPSSleepSleepMode)));
                        }
                    } catch (InterruptedException ex) {}
                }
            }, 1L, 1L);
        
        if (Optimizations.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep"))
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
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
