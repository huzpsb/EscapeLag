package com.mcml.space.features;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.Perms;

import static com.mcml.space.config.Features.preventSpawnerModify;
import static com.mcml.space.util.VersionLevel.modernApi;

import static org.bukkit.Material.*;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class SpawnerGuard implements Listener {
    public static void init(Plugin plugin) {
        if (!preventSpawnerModify) return;
        
        Bukkit.getPluginManager().registerEvents(new SpawnerGuard(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 刷怪保护 已启动" : "Submodule - SpawnerGuard has been enabled");
    }
    
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onModify(PlayerInteractEvent evt) {
        if (evt.getItem() == null || evt.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Perms.has(evt.getPlayer())) return;
        
        if (evt.getClickedBlock().getType() == (modernApi() ? LEGACY_MOB_SPAWNER : MOB_SPAWNER)) {
            Material type = evt.getItem().getType();
            if (modernApi() ?
                    type == LEGACY_MONSTER_EGG || type == LEGACY_MONSTER_EGGS :
                    type == MONSTER_EGG || type == MONSTER_EGGS) {
                evt.setCancelled(true);
            }
        }
    }
}
