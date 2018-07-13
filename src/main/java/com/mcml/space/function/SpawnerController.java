package com.mcml.space.function;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Perms;
import com.mcml.space.util.PluginExtends;

import static com.mcml.space.config.ConfigFunction.preventSpawnerModify;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class SpawnerController implements Listener, PluginExtends {
    public static void init(Plugin plugin) {
        if (!preventSpawnerModify) return;
        
        Bukkit.getPluginManager().registerEvents(new SpawnerController(), plugin);
        AzureAPI.log("刷怪笼控制模块已启动");
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onModify(PlayerInteractEvent evt) {
        if (evt.getItem() == null || evt.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (Perms.has(evt.getPlayer())) return;
        
        if (evt.getClickedBlock().getType() == Material.MOB_SPAWNER) {
            Material type = evt.getItem().getType();
            if (type == Material.MONSTER_EGG || type == Material.MONSTER_EGGS) {
                evt.setCancelled(true);
            }
        }
    }
}
