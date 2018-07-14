package com.mcml.space.optimizations;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.config.Optimizations;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.PluginExtends;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class EmptyRestart implements Listener, PluginExtends {
    public static void init(Plugin plugin) {
        if (!Optimizations.emptyRestart) return;
        
        Bukkit.getPluginManager().registerEvents(new EmptyRestart(), plugin);
        AzureAPI.log("无人重启模块已启动");
    }
    
    private static int restartTaskId = -1;
    
    @EventHandler
    public void preparRestart(PlayerQuitEvent evt){
        if(Optimizations.emptyRestart && restartTaskId == -1 && PlayerList.isEmpty()){
            restartTaskId = Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, new Runnable(){
                @Override
                public void run(){
                    AzureAPI.restartServer("服务器无人在线，开始重启...");
                }
            }, AzureAPI.toTicks(TimeUnit.SECONDS, Optimizations.emptyRestartDelay)).getTaskId();
        }
    }
    
    @EventHandler
    public void cancelRestart(PlayerJoinEvent evt){
        if(Optimizations.emptyRestart) {
            if (restartTaskId != -1) Bukkit.getScheduler().cancelTask(restartTaskId);
        }
    }
}