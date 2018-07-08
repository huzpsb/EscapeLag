package com.mcml.space.function;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcml.space.config.ConfigFunction;

public class FarmProtecter implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void EntityFarmChecker(EntityInteractEvent event){
        if(!ConfigFunction.ProtectFarmenable || event.getEntityType() == EntityType.PLAYER) return; // TODO 细分实体与玩家
        
        Block block = event.getBlock();
        if(block.getType() == Material.SOIL || block.getType() == Material.CROPS){
            event.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void PlayerFarmChecker(PlayerInteractEvent event){
        if(!ConfigFunction.ProtectFarmenable || event.getAction() != Action.PHYSICAL) return;
        
        Block block = event.getClickedBlock();
        if(block.getType() == Material.SOIL || block.getType() == Material.CROPS){
            event.setCancelled(true);
        }
    }
}
