package com.mcml.space.optimizations;

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

import com.mcml.space.config.Optimizations;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

public class ChunkUnloader implements Listener {
	public static long totalUnloadedChunks;

	public static void init(Plugin plugin) {
	    if (!Optimizations.chunkUnloader) return;
	    
	    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                for (World world : Bukkit.getWorlds()) {
                    for (Chunk chunk : world.getLoadedChunks())
                    if (!world.isChunkInUse(chunk.getX(), chunk.getZ())) {
                        if (chunk.unload(true)) totalUnloadedChunks++;
                    }
                }
                
                TimerGarbageCollect.collectGarbage();
            }
        }, 60);
	    
		Bukkit.getPluginManager().registerEvents(new ChunkUnloader(), plugin);
		AzureAPI.log("区块卸载系统现在运行...");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent evt) {
	    handleWorldChunks(evt.getFrom(), evt.getPlayer(), 0);
	}
	
	@EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        handleWorldChunks(evt.getPlayer().getLocation(), evt.getPlayer(), 1); // In case disconnect post to main thread
    }
	
	private static void handleWorldChunks(final Location from, final Player player, int delayedTicks) {
	    Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, new Runnable() {
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
        }, delayedTicks);
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
                Chunk chunk = world.getChunkAt(chunkX, chunkZ);
                
                if (ChunkKeeper.KEEP_LOADED_CHUNKS.contains(AzureAPI.wrapCoord(chunkX, chunkZ))) continue;
                if (world.isChunkInUse(chunkX, chunkZ)) {
                    chunk.unload();
                    totalUnloadedChunks++;
                }
            }
        }
	}
}
