package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Patches;

public class CancelledPlacementPatch implements Listener {
    public static void init(Plugin plugin) {
        if (!Patches.AntiInfSuagrenable) return;
        Bukkit.getPluginManager().registerEvents(new CancelledPlacementPatch(), plugin);
    }
	
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void place(BlockPlaceEvent evt) { // Not only door can trigger this!
        if(!evt.isCancelled()) return;
        
        Player player = evt.getPlayer();
        for (Entity drop : player.getNearbyEntities(2, 2, 2)) {
            if (drop.getType() != EntityType.DROPPED_ITEM) continue;
            
            org.bukkit.entity.Item item = (org.bukkit.entity.Item) drop;
            if (item.getItemStack().getType() == Material.SUGAR_CANE || item.getItemStack().getType() == Material.CACTUS) drop.remove();
        }
    }
}
