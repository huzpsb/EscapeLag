package com.mcml.space.optimizations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.PlayerList;
import com.mcml.space.core.Ticker;

public class TickSleep {
    private static volatile long sleepMillis = 20;
    
    public static void init(Plugin plugin) {
        if (Optimizes.TPSSleepNoOneFreezeenable || !Optimizes.TPSSleepSleepMode.equalsIgnoreCase("NoUse"))
            Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                try {
                    if (Optimizes.TPSSleepNoOneFreezeenable && PlayerList.isEmpty()) {
                        Thread.sleep(TickSleep.getTargetSleepTime(5));
                    }
                    
                    if (Optimizes.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep")) {
                        Thread.sleep(TickSleep.getTargetSleepTime(sleepMillis));
                    } else if (Optimizes.TPSSleepSleepMode.matches("[0-9]+")) {
                        Thread.sleep(TickSleep.getTargetSleepTime(Integer.parseInt(Optimizes.TPSSleepSleepMode)));
                    }
                } catch (InterruptedException ex) {}
            }, 1L, 1L);
        
        if (Optimizes.TPSSleepSleepMode.equalsIgnoreCase("AutoSleep"))
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
                if (sleepMillis < 20) {
                    if (sleepMillis - 1 < Ticker.getRealTimeTPS()) {
                        sleepMillis++;
                    } else {
                        sleepMillis = Ticker.getRealTimeTPS();
                    }
                } else {
                    if (Ticker.getRecentTPS() <= 18) sleepMillis = Ticker.getRealTimeTPS();
                }
            }, 40L, 40L);
    }
    
    static long getTargetSleepTime(long raw) {
        return (50 - ((5 * raw) / 2));
    }
}
