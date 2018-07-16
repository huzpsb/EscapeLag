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
import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.core.EscapeLag;

public class ContainerPortalPatch implements Listener {
    public static void init(Plugin plugin) {
        if(!PatchesDupeFixes.enablePortalContainerDupeFixes) return;
        Bukkit.getPluginManager().registerEvents(new ContainerPortalPatch(), plugin);
    }
    
    private final Set<InventoryHolder> containerInv = Sets.newSetFromMap(new java.util.IdentityHashMap<InventoryHolder, Boolean>(4));
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(final EntityPortalEvent evt) {
        EntityType type = evt.getEntityType();
        if (hasInventory(type)) {
            if (!handleContainer((InventoryHolder) evt.getEntity())) return;
            
            Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    containerInv.remove((InventoryHolder) evt.getEntity());
                    evt.getEntity().teleport(evt.getTo(), TeleportCause.NETHER_PORTAL); 
                }
            });
            evt.setCancelled(true);
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPortal(InventoryOpenEvent evt) {
        if (containerInv.contains(evt.getInventory().getHolder())) {
            evt.setCancelled(true);
        }
    }
    
    private boolean handleContainer(InventoryHolder holder) {
        containerInv.add(holder);
        for (HumanEntity player : holder.getInventory().getViewers()) {
            player.closeInventory();
        }
        return true;
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
}