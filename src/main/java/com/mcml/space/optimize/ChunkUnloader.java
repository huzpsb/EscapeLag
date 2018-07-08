package com.mcml.space.optimize;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.AzureAPI;

public class ChunkUnloader implements Listener {
	public static int ChunkUnloaderTimes;

	public static void init(Plugin JavaPlugin) {
		Bukkit.getPluginManager().registerEvents(new ChunkUnloader(), JavaPlugin);
		AzureAPI.log("区块卸载系统现在运行...");
	}
	
	/*被证实过于卡服！
	 * 
	 * @EventHandler
	public void onWorldLoad(WorldInitEvent event) {
		if(ConfigOptimize.chunkUnloader == true) {
			final World world = event.getWorld();
			Bukkit.getScheduler().runTaskTimer(EscapeLag.MainThis, new Runnable() {
				public void run() {
					Chunk[] loadedChunks = world.getLoadedChunks();
			        int lcl = loadedChunks.length;
			        for(int ii=0;ii<lcl;ii++){
			            Chunk chunk = loadedChunks[ii];
			            if(world.isChunkInUse(chunk.getX(),chunk.getZ())==false){
			                if(chunk.isLoaded() == true & ChunkKeeper.ShouldKeepList.contains(chunk)==false){
			                    chunk.unload();
			                    ChunkUnloaderTimes++;
			                }
			            }
			        }
				}
			}, 0, ConfigOptimize.ChunkUnloaderInterval);
		}
	}
	*/

	@EventHandler
	public void LeaveWorldCheck(PlayerChangedWorldEvent event) {
		if (ConfigOptimize.chunkUnloader == true && event.getFrom().getPlayers().isEmpty()) {
			Chunk[] loadedChunks = event.getFrom().getLoadedChunks();
			int lcl = loadedChunks.length;
			for (int i = 0; i < lcl; i++) {
				Chunk chunk = loadedChunks[i];
				if (chunk.isLoaded() == true & ChunkKeeper.ShouldKeepList.contains(chunk) == false) {
					chunk.unload();
					ChunkUnloaderTimes++;
				}
			}
		}
	}
}
