package com.mcml.space.optimize;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class OverloadRestart implements Runnable, PluginExtends {
    public static void init(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, new OverloadRestart(), 7 * 20, 7 * 20);
        
        AzureAPI.log("超负荷控制模块已启动");
    }
    
    @Override
    public void run() {
        if (ConfigOptimize.OverLoadMemoryRestartenable && isMemoryOverload()) {
            AzureAPI.bc(ConfigOptimize.OverLoadMemoryRestartWarnMessage);
            Bukkit.getServer().getScheduler().runTaskLater(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    if (!AzureAPI.restartServer(ConfigOptimize.OverLoadMemoryRestartKickMessage)) TimerGarbageCollect.collectGarbage();
                }
            }, ConfigOptimize.OverLoadMemoryRestartDelayTime * 20);
        }
    }
    
    public static boolean isMemoryOverload() {
        Runtime run = Runtime.getRuntime();
        return run.maxMemory() - run.totalMemory() + run.freeMemory() < ConfigOptimize.OverLoadMemoryRestartHeapMBLefted;
    }
    
}
