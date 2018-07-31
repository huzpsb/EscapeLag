package com.mcml.space.optimizations;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class TickSleep {

    public static void init(Plugin plugin) {
        if (Optimizes.TPSSleepNoOneFreezeenable) {
            Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    if (PlayerList.isEmpty()) {
                        try {
                            AzureAPI.bc("无人休眠");
                            Thread.sleep(TickSleep.getTargetSleepTime(5));
                        } catch (InterruptedException ex) {
                            Logger.getLogger(TickSleep.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        if (Optimizes.TPSSleepSleepMode.equalsIgnoreCase("NoUse") == false) {
                            try {
                                AzureAPI.bc("有人休眠");
                                Thread.sleep(TickSleep.getTargetSleepTime(Integer.parseInt(Optimizes.TPSSleepSleepMode)));
                            } catch (InterruptedException ex) {
                                Logger.getLogger(TickSleep.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }, 1, 1);
        }
    }

    static long getTargetSleepTime(long raw) {
        return (50 - ((5 * raw) / 2));
    }
}
