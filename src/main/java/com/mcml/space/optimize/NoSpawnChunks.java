package com.mcml.space.optimize;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.config.ConfigOptimize.noSpawnChunks;

public class NoSpawnChunks implements Listener {
    public static void init(Plugin plugin) {
        if (!noSpawnChunks) return;
        Bukkit.getPluginManager().registerEvents(new NoSpawnChunks(), plugin);
    }
    
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldInitEvent evt) {
        World world = evt.getWorld();
        world.setKeepSpawnInMemory(false);
        AzureAPI.log("已为世界 " + world.getName() + " 设定不保留出生区块.");
	}
}
