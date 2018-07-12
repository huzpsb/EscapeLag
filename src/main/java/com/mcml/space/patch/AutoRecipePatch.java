package com.mcml.space.patch;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import com.google.common.collect.Sets;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;
import com.mcml.space.util.VersionLevel;

public class AutoRecipePatch implements Listener, PluginExtends {
    public static final Set<String> RECIPE_KEEPERS = Sets.newHashSet();
    
    public static void init(Plugin plugin) {
        if (!VersionLevel.rawVersion().contains("(MC: 1.12)") || !Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) return;
        
        com.comphenix.protocol.ProtocolLibrary.getProtocolManager().addPacketListener(new com.comphenix.protocol.events.PacketAdapter(plugin, com.comphenix.protocol.PacketType.Play.Client.AUTO_RECIPE) {
            @Override
            public void onPacketReceiving(com.comphenix.protocol.events.PacketEvent evt) {
                RECIPE_KEEPERS.add(evt.getPlayer().getName());
            }
        });
        
        AzureAPI.log("自动合成修复模块已启用");
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent evt) {
        if (RECIPE_KEEPERS.contains(evt.getPlayer().getName())) evt.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent evt) {
        RECIPE_KEEPERS.remove(evt.getPlayer().getName());
    }
}