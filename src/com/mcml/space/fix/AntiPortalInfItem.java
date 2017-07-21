package com.mcml.space.fix;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;

import com.mcml.space.core.VLagger;
import com.mcml.space.util.ConfigNoBug;

public class AntiPortalInfItem implements Listener {

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        if (VLagger.AntiPortalInfItemenable == true) {
            if (event.getEntityType() == EntityType.MINECART_CHEST || event.getEntityType() == EntityType.MINECART_FURNACE || event.getEntityType() == EntityType.MINECART_HOPPER) {
                event.setCancelled(true);
                event.getEntity().remove();
                if(ConfigNoBug.AntiPortalInfItemWarnMessage.equalsIgnoreCase("none") == false){
                    Bukkit.broadcastMessage(VLagger.PluginPrefix + ConfigNoBug.AntiPortalInfItemWarnMessage);
                }
            }
        }
    }
}