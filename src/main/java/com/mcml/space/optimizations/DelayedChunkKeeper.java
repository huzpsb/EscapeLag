package com.mcml.space.optimizations;

import com.google.common.collect.Sets;
import com.mcml.space.config.OptimizesChunk;
import static com.mcml.space.config.OptimizesChunk.delayedChunkKeeper_maxUnloadChunksPerTick;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.Ticker;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.ChunkCoord;
import static com.mcml.space.util.VersionLevel.isPaper;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;

public class DelayedChunkKeeper implements Listener {
    @Nullable public final static Set<ChunkCoord> DEALYED_CHUNKS = isPaper() ? null : Sets.newHashSet();
    
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
            if (currentTick == Ticker.currentTick) {
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
                currentTick = Ticker.currentTick;
            }
        }
        
        // Dealyed unload
        if (!isPaper() && !DEALYED_CHUNKS.contains(coord)) {
            DEALYED_CHUNKS.add(coord);
            Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                if (world.isChunkLoaded(chunk) && !world.isChunkInUse(chunk.getX(), chunk.getZ())) chunk.unload();
                DEALYED_CHUNKS.remove(coord);
            }, AzureAPI.toTicks(TimeUnit.SECONDS, OptimizesChunk.delayedChunkKeeper_delayInSeconds));
        }
    }
}