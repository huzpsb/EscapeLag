package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.Plugin;

public class BonemealDupePatch implements Listener {
	public static void init(Plugin plugin) {
		Bukkit.getPluginManager().registerEvents(new BonemealDupePatch(), plugin);
	}
	
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onGrow(StructureGrowEvent evt) {
        if (!evt.isFromBonemeal()) return;
        
        for (BlockState state : evt.getBlocks()) {
            Material material = state.getBlock().getType();
            if (material != Material.AIR && material != Material.SAPLING) {
                evt.setCancelled(true);
            }
        }
    }
}