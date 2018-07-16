package com.mcml.space.optimizations;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Sets;
import com.mcml.space.config.OptimizesChunk;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI.ChunkCoord;

import com.mcml.space.util.Ticker;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.config.OptimizesChunk.delayedChunkKeeper_maxUnloadChunksPerTick;
import static com.mcml.space.util.VersionLevel.isPaper;

public class DelayedChunkKeeper implements Listener {
    public final static Set<ChunkCoord> DEALYED_CHUNKS = isPaper() ? Sets.newHashSet() : null;
    
    public static void init(Plugin plugin) {
        if (!OptimizesChunk.enableDelayedChunkKeeper
                && (delayedChunkKeeper_maxUnloadChunksPerTick <= 0 || isPaper())) return;
        Bukkit.getPluginManager().registerEvents(new DelayedChunkKeeper(), plugin);
    }
    
    private int unloadedChunks;
    private long currentTick = Integer.MIN_VALUE;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent evt) {
        World world = evt.getWorld();
        Chunk chunk = evt.getChunk();
        ChunkCoord coord = AzureAPI.wrapCoord(chunk.getX(), chunk.getZ());
        
        if (delayedChunkKeeper_maxUnloadChunksPerTick <= 0) {
            if (currentTick == Ticker.currentTick()) {
                if (++unloadedChunks > delayedChunkKeeper_maxUnloadChunksPerTick
                        && (isPaper() && !DEALYED_CHUNKS.contains(coord))) {
                    Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                        if (world.isChunkLoaded(chunk) && !world.isChunkInUse(chunk.getX(), chunk.getZ())) chunk.unload();
                    }, OptimizesChunk.delayedChunkKeeper_postSkipTicks);
                    evt.setCancelled(true);
                    return;
                }
            } else {
                unloadedChunks = 1;
                currentTick = Ticker.currentTick();
            }
        }
        
        // Dealyed unload
        if (!DEALYED_CHUNKS.contains(coord) && !isPaper()) {
            DEALYED_CHUNKS.add(coord);
            Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                if (world.isChunkLoaded(chunk) && !world.isChunkInUse(chunk.getX(), chunk.getZ())) chunk.unload();
                DEALYED_CHUNKS.remove(coord);
            }, AzureAPI.toTicks(TimeUnit.SECONDS, OptimizesChunk.delayedChunkKeeper_delayInSeconds));
        }
    }
}