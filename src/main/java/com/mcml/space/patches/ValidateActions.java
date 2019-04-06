package com.mcml.space.patches;

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

import com.mcml.space.config.Patches;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

import io.akarin.collect.set.player.MarkablePlayerSet;
import io.akarin.collect.set.player.PlayerSets;

public class ValidateActions implements Listener {
    public static void init(Plugin plugin) {
        if (!Patches.enableVaildateActions) return;
        Bukkit.getPluginManager().registerEvents(new ValidateActions(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 动作效验 已启动" : "Submodule - ActionValidate has been enabled");
    }
    
    private final MarkablePlayerSet invKeepers = PlayerSets.newInstanceBitSet(); // Handle this by nms is nice
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onOpenInventory(InventoryOpenEvent evt) {
        invKeepers.add((Player) evt.getPlayer());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCloseInventory(InventoryCloseEvent evt) {
        invKeepers.remove(evt.getPlayer());
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
    
    public void handlePlayerAction(Player player, Cancellable evt) {
        if (invKeepers.contains(player)) {
            evt.setCancelled(true);
            AzureAPI.warn("Player " + player.getName() + " acted action that impossible to happen (hack client?)");
        }
    }
}