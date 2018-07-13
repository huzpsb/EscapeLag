package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Hopper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.util.AzureAPI;

public class NetherHopperDupePatch implements Listener {
    
    public static void init(Plugin plugin) {
        if (!ConfigPatch.fixNetherHopperInfItem) return;
        
        Bukkit.getPluginManager().registerEvents(new NetherHopperDupePatch(), plugin);
        AzureAPI.log("地狱漏斗防护模块已开启");
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHopper(InventoryMoveItemEvent evt) {
        if (!(evt.getInitiator().getHolder() instanceof Hopper) || !(evt.getSource().getHolder() instanceof Hopper)) return;
        Hopper to = (Hopper) evt.getInitiator().getHolder();
        Hopper from = (Hopper) evt.getSource().getHolder();
        if (to.getWorld().getEnvironment() == World.Environment.NETHER) {
            if (to.getChunk() != from.getChunk()) evt.setCancelled(true);
        }
    }
    
}