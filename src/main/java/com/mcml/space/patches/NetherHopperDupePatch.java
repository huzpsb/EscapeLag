package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.Hopper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.config.PatchesDupeFixes.netherHoppersDupeFixes_Worlds;

public class NetherHopperDupePatch implements Listener {
    
    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableNetherHoppersDupeFixes) return;
        
        Bukkit.getPluginManager().registerEvents(new NetherHopperDupePatch(), plugin);
        AzureAPI.log("地狱漏斗防护模块已开启");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHopper(InventoryMoveItemEvent evt) {
        if (!(evt.getInitiator().getHolder() instanceof Hopper) || !(evt.getSource().getHolder() instanceof Hopper)) return;
        Hopper to = (Hopper) evt.getInitiator().getHolder();
        Hopper from = (Hopper) evt.getSource().getHolder();
        
        if (netherHoppersDupeFixes_Worlds.isEmpty() ||
                netherHoppersDupeFixes_Worlds.contains(from.getWorld().getName())) handleCancellation(from, to, evt.getItem(), evt);
    }
    
    private static void handleCancellation(Hopper from, Hopper to, ItemStack item, Cancellable evt) {
        if (PatchesDupeFixes.netherHoppersDupeFixes_RestrictEnv && from.getWorld().getEnvironment() != Environment.NETHER) return;
        if (to.getChunk() != from.getChunk()) evt.setCancelled(true);
        
        Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
            @Override
            public void run() {
                // Safely checks item
                from.getChunk().load();
                if (!from.getInventory().contains(item)) return;
                
                // Safely transfers item
                from.getInventory().remove(item);
                to.getChunk().load();
                to.getInventory().addItem(item);
            }
        });
    }
}