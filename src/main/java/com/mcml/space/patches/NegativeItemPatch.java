package com.mcml.space.patches;

import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class NegativeItemPatch implements Listener {

    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableNegativeItemDupeFixes
                && !Bukkit.getPluginManager().isPluginEnabled("RPGItems") && !Bukkit.getPluginManager().isPluginEnabled("RPG_Items")) {
            return;
        }

        Bukkit.getPluginManager().registerEvents(new NegativeItemPatch(), plugin);
        AzureAPI.log("负数物品修复模块已启用");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void DispenseCheck(BlockDispenseEvent evt) {
        if (evt.getItem().getAmount() > 0) {
            return; // Lazy check, performance
        }
        BlockState state = evt.getBlock().getState();
        if (!(state instanceof InventoryHolder)) {
            return;
        }

        Inventory Inventory = ((InventoryHolder) state).getInventory();
        for (ItemStack item : Inventory.getStorageContents()) {
            if (item.getAmount() > 0) {
                continue;
            }

            fliterPlayersInventory(null);
            removesNegativeDrops(evt.getBlock().getWorld());

            item.setAmount(1);
            evt.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void InteractCheck(PlayerInteractEvent evt) {
        if (evt.getItem() == null || evt.getItem().getAmount() > 0) {
            return;
        }

        fliterPlayersInventory(null);
        removesNegativeDrops(evt.getPlayer().getWorld());

        evt.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent evt) {
        if (evt.getItem().getItemStack().getAmount() > 0) {
            return;
        }

        removesNegativeDrops(evt.getItem().getWorld());
        fliterPlayersInventory(evt.getPlayer());
        AzureAPI.bc(PatchesDupeFixes.negativeItemDupeFixes_notifyMesssage, "$player", evt.getPlayer().getName());

        evt.setCancelled(true);
    }

    /**
     * Removes negative amount items from players inventories
     *
     * @param sourcePlayer to avoid double notify
     */
    private static void fliterPlayersInventory(@Nullable Player sourcePlayer) {
        if (!PatchesDupeFixes.negativeItemDupeFixes_fliterPlayersInv) {
            return;
        }
        
        PlayerList.forEach(player -> {
            boolean hasRemovedAny = false;
            for (ItemStack item : player.getInventory().getStorageContents()) {
                if (item.getAmount() <= 0) {
                    hasRemovedAny = true;
                    player.getInventory().remove(item);
                }
            }
            if (hasRemovedAny && sourcePlayer != null && !sourcePlayer.getName().equals(player.getName())) {
                AzureAPI.bc(PatchesDupeFixes.negativeItemDupeFixes_notifyMesssage, "$player", player.getName());
            }
        });
    }

    /**
     * Removes others dropped negative amount items
     *
     * @param sourceEntity to locate the world
     * @return whether removes item is allowed
     */
    public static boolean removesNegativeDrops(World world) {
        if (!PatchesDupeFixes.negativeItemDupeFixes_removesItem) {
            return false;
        }

        if (PatchesDupeFixes.negativeItemDupeFixes_removesItem_fliterDrops) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.DROPPED_ITEM) {
                    continue;
                }
                if (((Item) entity).getItemStack().getAmount() <= 0) {
                    entity.remove();
                }
            }
        }
        return true;
    }
}
