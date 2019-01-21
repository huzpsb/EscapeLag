package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;

import static com.mcml.space.util.VersionLevel.modernApi;

public class RailsMachine implements Listener {

    public static void init(Plugin plugin) {
        if (!PatchesDupeFixes.enableRailsMachineFixes) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new RailsMachine(), EscapeLag.plugin);
        AzureAPI.log(Locale.isNative() ? "子模块 - 铁轨机器 已启动" : "Submodule - RailsMachine has been enabled");
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPhysics(BlockPhysicsEvent evt) {
        Block block = evt.getBlock();
        if (!canExploit(block.getType())) {
            return;
        }

        Block layer = block.getRelative(BlockFace.DOWN);
        if (isLayer(layer.getType()) || isOrigin(evt.getChangedType())) {
            evt.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent evt) {
        final Block up = evt.getBlock().getRelative(BlockFace.UP);
        if (!canExploit(up.getType())) {
            return;
        }
        Bukkit.getScheduler().runTask(EscapeLag.plugin, up::breakNaturally);
    }

    @SuppressWarnings("deprecation")
    private static boolean canExploit(Material type) {
        if (type.getId() == 171 /* Carpet */) {
            return true;
        }

        switch (type) {
            case RAILS:
            case POWERED_RAIL:
            case DETECTOR_RAIL:
            case ACTIVATOR_RAIL:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isLayer(Material type) {
        if (type.getId() == 165 /* Slime block */) {
            return true;
        }

        switch (type) {
            case AIR:

            case PUMPKIN:
            case JACK_O_LANTERN:
            case MELON_BLOCK:

            case PISTON_BASE:
            case PISTON_MOVING_PIECE:
            case PISTON_STICKY_BASE:
            case PISTON_EXTENSION:
                return true;
            default:
                return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isOrigin(Material type) {
        if (type.getId() == 165 /* Slime block */) {
            return true;
        }

        switch (type) {
            case PUMPKIN:
            case JACK_O_LANTERN:
            case MELON_BLOCK:

            case PISTON_BASE:
            case PISTON_MOVING_PIECE:
            case PISTON_STICKY_BASE:
            case PISTON_EXTENSION:

            case DISPENSER:
            case DROPPER:
                return true;
            default:
                return false;
        }
    }
}
