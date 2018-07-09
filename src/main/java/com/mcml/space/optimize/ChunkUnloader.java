package com.mcml.space.optimize;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.AzureAPI;

public class ChunkUnloader implements Listener {
	public static long totalUnloadedChunks;

	public static void init(Plugin JavaPlugin) {
	    if (!ConfigOptimize.chunkUnloader) return;
	    
		Bukkit.getPluginManager().registerEvents(new ChunkUnloader(), JavaPlugin);
		AzureAPI.log("区块卸载系统现在运行...");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void teleport(PlayerTeleportEvent evt) {
        Player player = evt.getPlayer();
        Location from = evt.getFrom();
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
                
                if (ChunkKeeper.keepLoadedChunks.contains(AzureAPI.wrapCoord(chunkX, chunkZ))) continue;
                if (world.isChunkInUse(chunkX, chunkZ)) {
                    chunk.unload();
                    totalUnloadedChunks++;
                }
            }
        }
	}
}
