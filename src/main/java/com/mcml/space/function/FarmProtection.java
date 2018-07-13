package com.mcml.space.function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.config.ConfigFunction;

public class FarmProtection {
    public static void init(Plugin plugin) {
        if(!ConfigFunction.ProtectFarmenable) return;
        
        if (ConfigFunction.ProtectFarmOnlyPlayer) Bukkit.getPluginManager().registerEvents(new EntityDetector(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerDetector(), plugin);
    }
    
    private static class EntityDetector implements Listener {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onEntity(EntityInteractEvent evt) {
            if(evt.getEntityType() == EntityType.PLAYER) return;
            handleInteract(evt, evt.getBlock().getType());
        }
    }
    
    private static class PlayerDetector implements Listener {
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onPlayer(PlayerInteractEvent evt){
            if(evt.getAction() != Action.PHYSICAL) return;
            handleInteract(evt, evt.getClickedBlock().getType());
        }
    }
    
    private static void handleInteract(Cancellable evt, Material material) {
        if (material == Material.SOIL) evt.setCancelled(true);
    }
}