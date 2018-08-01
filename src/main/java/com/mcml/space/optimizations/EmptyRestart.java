package com.mcml.space.optimizations;

import com.mcml.space.config.Core;
import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class EmptyRestart implements Listener {
    public static void init(Plugin plugin) {
        if (!Optimizes.emptyRestart) return;
        
        Bukkit.getPluginManager().registerEvents(new EmptyRestart(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 闲置重启 已启动" : "Submodule - EmptyRestart has been enabled");
    }
    
    private static int restartTaskId = -1;
    
    @EventHandler
    public void preparRestart(PlayerQuitEvent evt){
        if(Optimizes.emptyRestart && restartTaskId == -1 && PlayerList.isEmpty()) {
            restartTaskId = Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> AzureAPI.restartServer("服务器无人在线，开始重启...", Core.hookRestartByScript),
                    AzureAPI.toTicks(TimeUnit.SECONDS, Optimizes.emptyRestartDelay)).getTaskId();
        }
    }
    
    @EventHandler
    public void cancelRestart(PlayerJoinEvent evt){
        if(Optimizes.emptyRestart) {
            if (restartTaskId != -1) Bukkit.getScheduler().cancelTask(restartTaskId);
        }
    }
}