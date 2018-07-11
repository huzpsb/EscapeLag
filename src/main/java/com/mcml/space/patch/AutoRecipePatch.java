package com.mcml.space.patch;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType.Play;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.google.common.collect.Sets;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;
import com.mcml.space.util.VersionLevel;

public class AutoRecipePatch implements Listener, PluginExtends {
    public static final Set<String> RECIPE_KEEPERS = Sets.newHashSet();
    
    public static void init(JavaPlugin plugin) {
        if (!VersionLevel.get().contains("(MC: 1.12)") || !Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) return;
        
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, Play.Client.AUTO_RECIPE) {
            @Override
            public void onPacketReceiving(PacketEvent evt) {
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