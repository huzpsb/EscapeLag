package com.mcml.space.optimizations;

import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.OptimizesChunk;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.util.VersionLevel.isPaper;

public class NoStyxChunks {
	public static long totalUnloadedChunks;

	public static void init(Plugin plugin) {
	    if (!OptimizesChunk.enableNoStyxChunks) return;
	    
	    if (OptimizesChunk.noStyxChunks_chancesInterval_enable)
	    Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks())
                    if (!world.isChunkInUse(chunk.getX(), chunk.getZ()) && (isPaper() || !DelayedChunkKeeper.DEALYED_CHUNKS.contains(AzureAPI.wrapCoord(chunk.getX(), chunk.getZ())))) { // Respect dealy
                        if (chunk.unload(true)) totalUnloadedChunks++;
                    }
                }
            }
        }, 60, AzureAPI.toTicks(TimeUnit.MINUTES, OptimizesChunk.noStyxChunks_chancesInterval_periods));
	    
		if (OptimizesChunk.noStyxChunks_chancesTeleport) Bukkit.getPluginManager().registerEvents(new TeleportDetector(), plugin);
		if (OptimizesChunk.noStyxChunks_chancesExit) Bukkit.getPluginManager().registerEvents(new ExitDetector(), plugin);
		
		AzureAPI.log("区块卸载系统现在运行...");
	}
	
	private static class TeleportDetector implements Listener {
	    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	    public void onTeleport(PlayerTeleportEvent evt) {
	        handleWorldChunks(evt.getFrom(), evt.getPlayer());
	    }
	}
	
	private static class ExitDetector implements Listener {
	    @EventHandler(priority = EventPriority.HIGHEST)
	    public void onQuit(PlayerQuitEvent evt) {
	        handleWorldChunks(evt.getPlayer().getLocation(), evt.getPlayer());
	    }
    }
	
	private static void handleWorldChunks(final Location from, final Player player) {
	    Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
            @Override
            public void run() {
                World world = from.getWorld();
                if (world.getPlayers().isEmpty()) {
                    for (Chunk each : world.getLoadedChunks()) {
                        each.unload();
                        totalUnloadedChunks++;
                    }
                } else {
                    unloadChunksAt(from, player);
                }
            }
        });
	}
	
	public static void unloadChunksAt(Location from, Player player) {
	    World world = from.getWorld();
	    
        int view = AzureAPI.viewDistanceBlock(player);
        int edgeX = from.getBlockX() + view;
        int edgeZ = from.getBlockZ() + view;
        
        for (int x = from.getBlockX() - view; x <= edgeX; x = x + 16) {
            for (int z = from.getBlockZ() - view; z <= edgeZ; z = z + 16) {
                int chunkX = x >> 4;
                int chunkZ = z >> 4;
                if (!isPaper() && DelayedChunkKeeper.DEALYED_CHUNKS.contains(AzureAPI.wrapCoord(chunkX, chunkZ))) continue; // Respect dealy
                
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                if (world.isChunkInUse(chunkX, chunkZ)) {
                    chunk.unload();
                    totalUnloadedChunks++;
                }
            }
        }
	}
}
