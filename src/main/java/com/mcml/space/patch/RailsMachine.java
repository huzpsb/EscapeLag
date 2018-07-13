package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.core.EscapeLag;

public class RailsMachine implements Listener {
    public static void init(Plugin plugin) {
        if (!ConfigPatch.fixInfRail) return;
        Bukkit.getPluginManager().registerEvents(new RailsMachine(), EscapeLag.plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent evt) {
        Block block = evt.getBlock();
        if (!canExploit(block.getType())) return;
        
        Block layer = block.getRelative(BlockFace.DOWN);
        if (isLayer(layer.getType()) || isOrigin(evt.getChangedType())) evt.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent evt) {
        final Block up = evt.getBlock().getRelative(BlockFace.UP);
        if (!canExploit(up.getType())) return;
        
        Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
            @Override
            public void run() {
                up.breakNaturally();
            }
        });
    }
    
    @SuppressWarnings("deprecation")
    private static boolean canExploit(Material type) {
        if (type.getId() != 171 /* Carpet */) return true;
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
    
    @SuppressWarnings("deprecation")
    private static boolean isLayer(Material type) {
        if (type.getId() != 165 /* Slime block */) return true;
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
    
    @SuppressWarnings("deprecation")
    private static boolean isOrigin(Material type) {
        if (type.getId() != 165 /* Slime block */) return true;
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
}