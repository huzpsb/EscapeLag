package com.mcml.space.optimize;

import static com.mcml.space.config.ConfigFunction.emptyRestart;
import static com.mcml.space.config.ConfigFunction.emptyRestartDelay;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.config.ConfigFunction;
import com.mcml.space.core.VLagger;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzurePlayerList;
import com.mcml.space.util.VersionLevel;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class EmptyRestart implements Listener {
    public static void init(JavaPlugin plugin) {
        if (ConfigFunction.emptyRestartHookSpigot && !VersionLevel.isSpigot()) {
            AzureAPI.warn("非 Spigot 服务端不支持无人重启绑定, 需要手动设置自重启脚本");
        }
        
        Bukkit.getPluginManager().registerEvents(new EmptyRestart(), plugin);
        AzureAPI.log("无人重启模块已启动");
    }
    
    private static int restartTaskId = -1;

    @EventHandler(priority = EventPriority.MONITOR)
    public void preparRestart(PlayerQuitEvent evt){
        if(AzurePlayerList.isEmpty() && emptyRestart && restartTaskId == -1){
            restartTaskId = Bukkit.getScheduler().runTaskLater(VLagger.MainThis, new Runnable(){
                @Override
                public void run(){
                    AzureAPI.tryRestartServer();
                }
            }, AzureAPI.toTicks(TimeUnit.SECONDS, emptyRestartDelay)).getTaskId();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void cancelRestart(PlayerJoinEvent evt){
        if(emptyRestart) {
            Bukkit.getScheduler().cancelTask(restartTaskId);
            restartTaskId = -1;
        }
    }
    
}