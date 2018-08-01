package com.mcml.space.patches;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import com.comphenix.protocol.PacketType.Play;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.CallerSensitive;

import io.akarin.collect.set.player.MarkablePlayerSet;
import io.akarin.collect.set.player.PlayerSets;

public class AutoRecipePatch implements Listener {
    public static final MarkablePlayerSet RECIPE_KEEPERS = PlayerSets.newInstanceBitSet();
    
    @CallerSensitive(version = "1.12")
    public static void init(Plugin plugin) {
        if (!VersionLevel.rawVersion().contains("(MC: 1.12)")) return;
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, Play.Client.AUTO_RECIPE) {
            @Override
            public void onPacketReceiving(PacketEvent evt) {
                RECIPE_KEEPERS.add(evt.getPlayer());
            }
        });
        AzureAPI.log(Locale.isNative() ? "子模块 - 合成修复 已启动" : "Submodule - AutoRecipePatch has been enabled");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent evt) {
        if (RECIPE_KEEPERS.contains(evt.getPlayer())) evt.setCancelled(true);
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onClose(InventoryCloseEvent evt) {
        RECIPE_KEEPERS.remove(evt.getPlayer());
    }
}