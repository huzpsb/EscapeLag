package com.mcml.space.optimize;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.util.AzureAPI;


import static com.mcml.space.config.ConfigOptimize.noSpawnChunks;

public class NoSpawnChunks implements Listener {
    public static void init(JavaPlugin plugin) {
        if (!noSpawnChunks) return;
        Bukkit.getPluginManager().registerEvents(new NoSpawnChunks(), plugin);
    }
    
	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorldLoad(WorldInitEvent evt) {
        World world = evt.getWorld();
        world.setKeepSpawnInMemory(false);
        AzureAPI.log("已为世界 " + world.getName() + " 设定不保留出生区块.");
	}
}
