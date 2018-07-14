package com.mcml.space.optimizations;

import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.mcml.space.config.Optimizations;
import com.mcml.space.util.AzureAPI.ChunkCoord;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;

public class ChunkKeeper implements Listener {
    public final static Set<ChunkCoord> KEEP_LOADED_CHUNKS = Sets.newSetFromMap(new WeakHashMap<ChunkCoord, Boolean>());
    
    public static void init(Plugin plugin) {
        if (!Optimizations.ChunkKeeperenable) return;
        
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                PlayerList.forEach(new Predicate<Player>() {
                    @Override
                    public boolean apply(Player player) {
                        Chunk chunk = player.getLocation().getChunk();
                        KEEP_LOADED_CHUNKS.add(AzureAPI.wrapCoord(chunk.getX(), chunk.getZ()));
                        return true;
                    }
                });
            }
        }, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, 15));
        
        Bukkit.getPluginManager().registerEvents(new ChunkKeeper(), plugin);
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        if (KEEP_LOADED_CHUNKS.contains(AzureAPI.wrapCoord(chunk.getX(), chunk.getZ()))) {
            event.setCancelled(true);
        }
    }
}
