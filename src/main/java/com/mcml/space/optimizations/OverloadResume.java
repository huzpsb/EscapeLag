package com.mcml.space.optimizations;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class OverloadResume implements Runnable {
    public static void init(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, new OverloadResume(), 7 * 20, 7 * 20);
        AzureAPI.log(Locale.isNative() ? "子模块 - 濒危抢救 已启动" : "Submodule - OverloadResume has been enabled");
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
