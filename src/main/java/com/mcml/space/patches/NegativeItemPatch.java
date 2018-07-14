package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Predicate;
import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.PluginExtends;

public class NegativeItemPatch implements Listener, PluginExtends {
    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableNegativeItemDupeFixes &&
                !Bukkit.getPluginManager().isPluginEnabled("RPGItems") && !Bukkit.getPluginManager().isPluginEnabled("RPG_Items")) return;
        
        Bukkit.getPluginManager().registerEvents(new NegativeItemPatch(), plugin);
        AzureAPI.log("RPGItem 修复模块已启用");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(final PlayerPickupItemEvent evt) {
        if (evt.getItem().getItemStack().getAmount() <= 0) {
            handleRemoves(evt.getItem());
            if (PatchesDupeFixes.negativeItemDupeFixes_FliterPlayersInv) {
                PlayerList.forEach(new Predicate<Player>() {
                    @Override
                    public boolean apply(Player player) {
                        boolean notify = false;
                        for (ItemStack item : player.getInventory().getStorageContents()) {
                            if (item.getAmount() <= 0) {
                                notify = true;
                                player.getInventory().remove(item);
                            }
                        }
                        if (notify && !evt.getPlayer().getName().equals(player.getName())) AzureAPI.bc(PatchesDupeFixes.negativeItemDupeFixes_NotifyMesssage, "$player", player.getName());
                        return true;
                    }
                });
            }
            AzureAPI.bc(PatchesDupeFixes.negativeItemDupeFixes_NotifyMesssage, "$player", evt.getPlayer().getName());
            evt.setCancelled(true);
        }
    }
    
    private static void handleRemoves(Item itemEntity) {
        if (!PatchesDupeFixes.negativeItemDupeFixes_RemovesItem) return;
        
        if (PatchesDupeFixes.negativeItemDupeFixes_RemovesItem_FliterDrops) {
            for (Entity entity : itemEntity.getWorld().getEntities()) {
                if (entity.getType() != EntityType.DROPPED_ITEM) continue;
                if (((Item) entity).getItemStack().getAmount() <= 0) entity.remove();
            }
        } else itemEntity.remove();
    }
}