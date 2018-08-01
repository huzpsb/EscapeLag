package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.EscapeLag;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

public class ZeroHealthPatch implements Listener {
    public static void init(Plugin plugin) {
        if (!Patches.noFakedeath) return;
        Bukkit.getPluginManager().registerEvents(new ZeroHealthPatch(), plugin);
    }

    public static void clearUndeadPlayer(Player player) {
        Bukkit.getScheduler().runTask(EscapeLag.plugin, () -> {
            if (Double.isNaN(player.getHealth()) || Double.isInfinite(player.getHealth())) {
                player.setHealth(0.0D);
                player.kickPlayer(Patches.messageFakedeath);
            }
        });
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByBlockEvent evt) {
        if (evt.getEntityType() == EntityType.PLAYER) clearUndeadPlayer((Player) evt.getEntity());
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent evt) {
        if (evt.getEntityType() == EntityType.PLAYER) clearUndeadPlayer((Player) evt.getEntity());
    }
}
