package com.mcml.space.patch;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import static com.mcml.space.config.ConfigPatch.noSkullCrash;

/**
 * @author jiongjionger
 */
public class SkullCrashPatch implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void NoSkullCrash(BlockFromToEvent evt) {
        if (noSkullCrash) {
            if (evt.getToBlock().getType() == Material.SKULL) {
                evt.setCancelled(true);
            }
        }
    }
}