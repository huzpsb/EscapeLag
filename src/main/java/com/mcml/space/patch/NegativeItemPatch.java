package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.config.ConfigPatch;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;

import static com.mcml.space.config.ConfigPatch.fixRPGItemInfItem;

/**
 * @author Vlvxingze
 */
public class NegativeItemPatch implements Listener, PluginExtends {
    public static void init(Plugin plugin) {
        if (!fixRPGItemInfItem && !Bukkit.getPluginManager().isPluginEnabled("RPGItems") && !Bukkit.getPluginManager().isPluginEnabled("RPG_Items")) return;
        
        Bukkit.getPluginManager().registerEvents(new NegativeItemPatch(), plugin);
        AzureAPI.log("RPGItem 修复模块已启用");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(PlayerPickupItemEvent evt) {
        if (evt.getItem().getItemStack().getAmount() <= 0) {
            if (ConfigPatch.forceRPGItemPatch) evt.getItem().remove();
            evt.setCancelled(true);
        }
    }
}