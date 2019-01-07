package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

import static com.mcml.space.config.PatchesDupeFixes.cancelledPlacementDupeFixes_clearsRadius;

import static com.mcml.space.util.VersionLevel.modernApi;
import static org.bukkit.Material.*;

import java.util.Map;

public class CancelledPlacementPatch implements Listener {

    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableCancelledPlacementDupeFixes) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new CancelledPlacementPatch(), plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 取消放置 已启动" : "Submodule - CancelledPlacementPatch has been enabled");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void place(BlockPlaceEvent evt) { // Not only door can trigger this!
        if (!evt.isCancelled()) {
            return;
        }

        Player player = evt.getPlayer();
        Map<String, String> radius = cancelledPlacementDupeFixes_clearsRadius;
        for (Entity drop : player.getNearbyEntities(Integer.valueOf(radius.get("x")), Integer.valueOf(radius.get("y")), Integer.valueOf(radius.get("z")))) {
            if (drop.getType() != EntityType.DROPPED_ITEM) {
                continue;
            }

            org.bukkit.entity.Item item = (org.bukkit.entity.Item) drop;
            Material material = item.getItemStack().getType();

            if (modernApi()
                    ? material == LEGACY_SUGAR_CANE || material == LEGACY_CACTUS
                    : material == SUGAR_CANE || material == CACTUS) {
                drop.remove();
            }
        }
    }
}
