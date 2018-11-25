package com.mcml.space.optimizations;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

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
                AzureAPI.restartServer(Optimizes.OverLoadMemoryRestartKickMessage);
            }, Optimizes.OverLoadMemoryRestartDelayTime * 20);
        }
    }

    public static boolean isMemoryOverload() {
        Runtime run = Runtime.getRuntime();
        return (run.maxMemory() - run.totalMemory() + run.freeMemory()) / 1024 / 1024 < Optimizes.OverLoadMemoryRestartHeapMBLefted;
    }
}
