package com.mcml.space.features;

import com.mcml.space.config.Features;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import static com.mcml.space.util.VersionLevel.modernApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import static org.bukkit.Material.*;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class FarmProtect {

    public static void init(Plugin plugin) {
        if (Features.ProtectFarmenable) {
            Bukkit.getPluginManager().registerEvents(new EntityDetector(), plugin);
            Bukkit.getPluginManager().registerEvents(new PlayerDetector(), plugin);
            AzureAPI.log(Locale.isNative() ? "子模块 - 耕地保护 已启动" : "Submodule - FarmProtect has been enabled");
        }
    }

    private static class EntityDetector implements Listener {

        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onEntity(EntityInteractEvent evt) {
            if (evt.getEntityType() == EntityType.PLAYER) {
                return;
            }
            handleInteract(evt, evt.getBlock().getType());
        }
    }

    private static class PlayerDetector implements Listener {

        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void onPlayer(PlayerInteractEvent evt) {
            if (evt.getAction() != Action.PHYSICAL) {
                return;
            }
            handleInteract(evt, evt.getClickedBlock().getType());
        }
    }

    private static void handleInteract(Cancellable evt, Material material) {
        if (material == Material.SOIL) {
            evt.setCancelled(true);
        }
    }
}
