package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.core.EscapeLag;

public class RailsMachine implements Listener {
    public static void init(Plugin plugin) {
        if (!ConfigPatch.fixInfRail) return;
        Bukkit.getPluginManager().registerEvents(new RailsMachine(), EscapeLag.plugin);
    }
    
    private static boolean canExploit(Material type) {
        switch (type) {
            case RAILS:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case ACTIVATOR_RAIL:
                return true;
            default:
                return false;
        }
    }
    
    private static boolean isLayer(Material type) {
        switch (type) {
            case AIR:
                
            case PUMPKIN:
            case JACK_O_LANTERN:
            case MELON_BLOCK:
                
            case PISTON_BASE:
            case PISTON_MOVING_PIECE:
            case PISTON_STICKY_BASE:
            case PISTON_EXTENSION:
                return true;
            default:
                return false;
        }
    }
    
    private static boolean isOrigin(Material type) {
        switch (type) {
            case PUMPKIN:
            case JACK_O_LANTERN:
            case MELON_BLOCK:
                
            case PISTON_BASE:
            case PISTON_MOVING_PIECE:
            case PISTON_STICKY_BASE:
            case PISTON_EXTENSION:
                
            case DISPENSER:
            case DROPPER:
                return true;
            default:
                return false;
        }
    }
    
	@SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent evt) {
        Block block = evt.getBlock();
        if (block.getTypeId() != 165 /* Slime block */ && !canExploit(block.getType())) return;
        
        Block layer = block.getRelative(BlockFace.DOWN);
        if (layer.getTypeId() == 165 || evt.getChangedTypeId() == 165 /* Slime block */
                || isLayer(layer.getType()) || isOrigin(evt.getChangedType())) evt.setCancelled(true);
    }
}