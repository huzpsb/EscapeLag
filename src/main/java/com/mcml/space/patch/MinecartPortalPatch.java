package com.mcml.space.patch;

import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mcml.space.config.ConfigPatch;
import com.mcml.space.core.EscapeLag;

public class MinecartPortalPatch implements Listener {
    private final static Set<InventoryHolder> MINECART_INV = Sets.newSetFromMap(Maps.<InventoryHolder, Boolean>newIdentityHashMap());
    
    public static void init(JavaPlugin plugin) {
        if(!ConfigPatch.fixPortalInfItem) return;
        Bukkit.getPluginManager().registerEvents(new MinecartPortalPatch(), plugin);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(final EntityPortalEvent evt) {
        EntityType type = evt.getEntityType();
        if (type == EntityType.MINECART_CHEST || type == EntityType.MINECART_FURNACE || type == EntityType.MINECART_HOPPER) {
            if (!handleMinecart((InventoryHolder) evt.getEntity())) return;
            
            Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    MINECART_INV.remove((InventoryHolder) evt.getEntity());
                    evt.getEntity().teleport(evt.getTo(), TeleportCause.NETHER_PORTAL); 
                }
            });
            evt.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(InventoryOpenEvent evt) {
        if (MINECART_INV.contains(evt.getInventory().getHolder())) {
            evt.setCancelled(true);
        }
    }
    
    private static boolean handleMinecart(InventoryHolder holder) {
        if (holder.getInventory().getContents() == null) return false; // Skip empty minecarts
        MINECART_INV.add(holder);
        for (HumanEntity player : holder.getInventory().getViewers()) {
            player.closeInventory();
        }
        return true;
    }
}