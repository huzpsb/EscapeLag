package com.mcml.space.optimizations;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    
    @EventHandler
    public void preparRestart(PlayerQuitEvent evt){
        if(PlayerList.isEmpty()){
            AzureAPI.restartServer("服务器无人,正在重启服务器...");
        }
    }
}