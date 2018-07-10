package com.mcml.space.optimize;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

public class SlackRedstone implements Listener {
    private final static HashMap<Location, Integer> CHECKED_TIMES = Maps.newHashMap();

    public static void init(Plugin plugin){
        if(!ConfigOptimize.AntiRedstoneenable) return;
        
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){
            @Override
            public void run(){
                CHECKED_TIMES.clear();
            }
        }, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, 7));
        
        Bukkit.getPluginManager().registerEvents(new SlackRedstone(), plugin);
    }

    @EventHandler
    public void CheckRedstone(BlockRedstoneEvent evt){
        if(evt.getOldCurrent() > evt.getNewCurrent()) return;
        
        final Block block = evt.getBlock();
        Location location = block.getLocation();
        
        Integer times = CHECKED_TIMES.get(location);
        CHECKED_TIMES.put(location, times == null ? (times = 1) : ++times);
        if (times <= ConfigOptimize.AntiRedstoneTimes) return;
        
        if (ConfigOptimize.AntiRedstoneRemoveBlockList.contains(block.getType().name())){
            Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable(){
                public void run(){
                    block.setType(Material.AIR);
                }
            });
            
            String message = ConfigOptimize.AntiRedstoneMessage;
            message = StringUtils.replace(message, "%location%", location.toString());
            AzureAPI.bc(message);
        }
    }
}
