package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.block.Hopper;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.util.AzureAPI;

import static com.mcml.space.config.PatchesDupeFixes.netherHoppersDupeFixesWorlds;;

public class NetherHopperDupePatch implements Listener {
    
    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableNetherHoppersDupeFixes) return;
        
        Bukkit.getPluginManager().registerEvents(new NetherHopperDupePatch(), plugin);
        AzureAPI.log("地狱漏斗防护模块已开启");
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onHopper(InventoryMoveItemEvent evt) {
        if (!(evt.getInitiator().getHolder() instanceof Hopper) || !(evt.getSource().getHolder() instanceof Hopper)) return;
        Hopper to = (Hopper) evt.getInitiator().getHolder();
        Hopper from = (Hopper) evt.getSource().getHolder();
        
        if (netherHoppersDupeFixesWorlds.isEmpty() ||
                netherHoppersDupeFixesWorlds.contains(from.getWorld().getName())) handleCancellation(from, to, evt);
    }
    
    private static void handleCancellation(Hopper from, Hopper to, Cancellable evt) {
        if (PatchesDupeFixes.netherHoppersDupeFixesRestrictEnv && from.getWorld().getEnvironment() != Environment.NETHER) return;
        if (to.getChunk() != from.getChunk()) evt.setCancelled(true);
    }
}