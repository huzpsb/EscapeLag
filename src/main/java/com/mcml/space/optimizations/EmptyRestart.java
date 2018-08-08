package com.mcml.space.optimizations;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class EmptyRestart implements Listener {
    public static void init(Plugin plugin) {
        if (!Optimizes.emptyRestart) return;
        
        Bukkit.getPluginManager().registerEvents(new EmptyRestart(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 闲置重启 已启动" : "Submodule - EmptyRestart has been enabled");
    }
    private BukkitTask RestartTask = null;
    
    @EventHandler
    public void stopRestartTask(PlayerJoinEvent event){
        if(RestartTask != null){
            RestartTask.cancel();
            RestartTask = null;
        }
    }
    
    @EventHandler
    public void preparRestart(PlayerQuitEvent evt){
        if(PlayerList.isEmpty()){
            RestartTask = Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    AzureAPI.restartServer("服务器无人,正在重启服务器...");
                }
            }, 5 * 60 * 20);
        }
    }
}