package com.mcml.space.optimizations;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;
import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.util.VersionLevel.modernApi;
import static org.bukkit.Material.*;

public class RedstoneSlacker implements Listener {
    private final static HashMap<Location, Integer> CHECKED_TIMES = Maps.newHashMap();
    private static int maxCounts;
    
    public static void init(Plugin plugin){
        if(!Optimizes.AntiRedstoneenable) return;
        
        Bukkit.getScheduler().runTaskTimer(plugin, CHECKED_TIMES::clear, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, Optimizes.AntiRedstoneTimes));
        maxCounts = Optimizes.AntiRedstoneTimes * 4;
        
        Bukkit.getPluginManager().registerEvents(new RedstoneSlacker(), plugin);
    }
    
    private Location notifyLocation;
    
    // High priority for avoid dupe!
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onRedstone(BlockRedstoneEvent evt){
        if(evt.getOldCurrent() > evt.getNewCurrent()) return;
        
        final Block block = evt.getBlock();
        Location location = block.getLocation();
        
        Integer times = CHECKED_TIMES.get(location);
        CHECKED_TIMES.put(location, times == null ? (times = 0) : ++times);
        if (times <= maxCounts) return;
        
        if (Optimizes.AntiRedstoneRemoveBlockList.contains(block.getType().name())){
            Bukkit.getScheduler().runTask(EscapeLag.plugin, () -> {
                if (Optimizes.dropRedstone) {
                    block.breakNaturally();
                } else {
                    block.setType(modernApi() ? LEGACY_AIR : AIR);
                }
            });
            
            // Skip close locations
            if (notifyLocation != null && notifyLocation.getWorld().equals(location.getWorld()) && notifyLocation.distance(location) <= 16) {
                notifyLocation = location;
                return;
            }
            
            String message = Optimizes.AntiRedstoneMessage;
            message = StringUtils.replace(message, "%location%",
                    "(" + location.getWorld().getName() + ": " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")");
            AzureAPI.bc(message);
        }
    }
}
