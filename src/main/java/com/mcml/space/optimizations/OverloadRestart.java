package com.mcml.space.optimizations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class OverloadRestart implements Runnable, PluginExtends {
    public static void init(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, new OverloadRestart(), 7 * 20, 7 * 20);
        AzureAPI.log("超负荷控制模块已启动");
    }
    
    @Override
    public void run() {
        if (Optimizes.OverLoadMemoryRestartenable && isMemoryOverload()) {
            AzureAPI.bc(Optimizes.OverLoadMemoryRestartWarnMessage);
            Bukkit.getServer().getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                if (!AzureAPI.restartServer(Optimizes.OverLoadMemoryRestartKickMessage, Core.hookRestartByScript)) TimerGarbageCollect.collectGarbage();
            }, Optimizes.OverLoadMemoryRestartDelayTime * 20);
        }
    }
    
    public static boolean isMemoryOverload() {
        Runtime run = Runtime.getRuntime();
        return run.maxMemory() - run.totalMemory() + run.freeMemory() < Optimizes.OverLoadMemoryRestartHeapMBLefted;
    }
}
