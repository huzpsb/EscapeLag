package com.mcml.space.optimize;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;
import com.mcml.space.config.Optimizations;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.ChunkCoord;

public class FireSpreadSlacker implements Listener {
    private final static HashMap<ChunkCoord, Long> CHECKED_CHUNKS = Maps.newHashMap();
    
    public static void init(Plugin plugin) {
        if (!Optimizations.FireLimitorenable) return;
        Bukkit.getPluginManager().registerEvents(new FireSpreadSlacker(), plugin);
        
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                CHECKED_CHUNKS.clear();
            }
        }, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, 90));
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSpread(BlockIgniteEvent event) {
        if (event.getCause() != IgniteCause.SPREAD) return;
        
        Chunk chunk = event.getBlock().getChunk();
        if (isFireOverclock(AzureAPI.wrapCoord(chunk.getX(), chunk.getZ()))) event.setCancelled(true);
    }
    
    public static boolean isFireOverclock(ChunkCoord coord) {
        Long checked = CHECKED_CHUNKS.get(coord);
        if (checked == null) {
            CHECKED_CHUNKS.put(coord, System.currentTimeMillis());
            return false;
        }
        return checked.longValue() + Optimizations.FireLimitorPeriod >= System.currentTimeMillis();
    }
}
