package com.mcml.space.optimize;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

import com.mcml.space.util.AzureAPI;

import lombok.val;

import static com.mcml.space.config.ConfigOptimize.noSpawnChunks;
import static com.mcml.space.config.ConfigOptimize.nscExcludeWorlds;

public class NoSpawnChunks implements Listener {
    
    @EventHandler
    public void onWorldLoad(WorldInitEvent evt) {
        if (noSpawnChunks) {
            val world = evt.getWorld();
            if (nscExcludeWorlds.isEmpty() || !AzureAPI.containsIgnoreCase(nscExcludeWorlds, world.getName())) {
                world.setKeepSpawnInMemory(false);
                AzureAPI.log("已为世界 " + world.getName() + " 设定不保留出生区块.");
            }
        }
    }
}
