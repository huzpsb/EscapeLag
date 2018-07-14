package com.mcml.space.patches;

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
import org.bukkit.plugin.Plugin;
import com.google.common.collect.Sets;
import com.mcml.space.config.Patches;
import com.mcml.space.core.EscapeLag;

public class ContainerPortalPatch implements Listener {
    private final static Set<InventoryHolder> CONTAINER_INV = Sets.newSetFromMap(new java.util.IdentityHashMap<InventoryHolder, Boolean>(4));
    
    public static void init(Plugin plugin) {
        if(!Patches.fixPortalInfItem) return;
        Bukkit.getPluginManager().registerEvents(new ContainerPortalPatch(), plugin);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(final EntityPortalEvent evt) {
        EntityType type = evt.getEntityType();
        if (hasInventory(type)) {
            if (!handleContainer((InventoryHolder) evt.getEntity())) return;
            
            Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    CONTAINER_INV.remove((InventoryHolder) evt.getEntity());
                    evt.getEntity().teleport(evt.getTo(), TeleportCause.NETHER_PORTAL); 
                }
            });
            evt.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(InventoryOpenEvent evt) {
        if (CONTAINER_INV.contains(evt.getInventory().getHolder())) {
            evt.setCancelled(true);
        }
    }
    
    private static boolean hasInventory(EntityType type) {
        switch (type) {
            case MINECART_CHEST:
            case MINECART_FURNACE:
            case MINECART_HOPPER:
            case VILLAGER:
            case HORSE:
                return true;
            default:
                return false;
        }
    }
    
    private static boolean handleContainer(InventoryHolder holder) {
        if (holder.getInventory().getContents() == null) return false; // Skip empty minecarts
        CONTAINER_INV.add(holder);
        for (HumanEntity player : holder.getInventory().getViewers()) {
            player.closeInventory();
        }
        return true;
    }
}