package com.mcml.space.patches;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Sets;
import com.mcml.space.config.Patches;
import com.mcml.space.util.AzureAPI;

public class ValidateActions implements Listener {
    private final static Set<String> INV_KEEPERS = Sets.newHashSet(); // Handle this by nms is nice
    
    public static void init(Plugin plugin) {
        if (!Patches.enableVaildateActions) return;
        
        AzureAPI.log("动作效验模块已启用!");
        Bukkit.getPluginManager().registerEvents(new ValidateActions(), plugin);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpenInventory(InventoryOpenEvent evt) {
        INV_KEEPERS.add(evt.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCloseInventory(InventoryCloseEvent evt) {
        INV_KEEPERS.remove(evt.getPlayer().getName());
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent evt) {
        evt.getPlayer().closeInventory();
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractBlock(PlayerInteractEvent evt) {
        handlePlayerAction(evt.getPlayer(), evt);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractEntity(PlayerInteractEntityEvent evt) {
        handlePlayerAction(evt.getPlayer(), evt);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSlotSwitch(PlayerItemHeldEvent evt) {
        handlePlayerAction(evt.getPlayer(), evt);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent evt) {
        handlePlayerAction(evt.getPlayer(), evt);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent evt) {
        handlePlayerAction(evt.getPlayer(), evt);
    }
    
    public static void handlePlayerAction(Player player, Cancellable evt) {
        if (INV_KEEPERS.contains(player.getName())) {
            evt.setCancelled(true);
            AzureAPI.warn("Player " + player.getName() + " acted action that impossible to happen (hack client?)");
        }
    }
}