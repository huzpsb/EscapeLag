package com.mcml.space.optimize;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.google.common.collect.Lists;
import com.mcml.space.core.VLagger;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.Coord2D;

import lombok.val;

/**
 * @author SotrForgotten
 */
public class TeleportPreloader implements Listener {
    public static final Map<Location, List<Coord2D>> caches = new WeakHashMap<Location, List<Coord2D>>();
    protected volatile static boolean pending;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(PlayerTeleportEvent evt) {
        if (evt.isCancelled() || evt.isAsynchronous() || pending || !VLagger.TeleportPreLoaderenable) return;

        val from = evt.getFrom();
        val to = evt.getTo();
        val player = evt.getPlayer();
        if (from.equals(to)) {
            evt.setCancelled(true);
            return;
        }
        if (!canPreload(from, to, player)) {
            return;
        }
        evt.setCancelled(true);

        val world = player.getWorld();

        boolean custom = AzureAPI.customViewDistance(player);
        List<Coord2D> chunks = custom ? collectPreloadChunks(to, player) : caches.get(to);
        if (chunks == null) {
            chunks = collectPreloadChunks(to, player);
            caches.put(to, chunks);
        }
        val fChunks = chunks;
        val total = chunks.size();
        val preChunks = total / 3;
        val secondStage = preChunks * 2;

        player.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(VLagger.MainThis, new Runnable() {
            @Override
            public void run() {
                Coord2D coord;
                for (int i = 0; i < preChunks; i++) {
                    coord = fChunks.get(i);
                    world.getChunkAt(coord.getX(), coord.getZ());
                }
            }
        }, 1L);
        Bukkit.getScheduler().runTaskLater(VLagger.MainThis, new Runnable() {
            @Override
            public void run() {
                Coord2D coord;
                for (int i = preChunks; i < secondStage; i++) {
                    coord = fChunks.get(i);
                    world.getChunkAt(coord.getX(), coord.getZ());
                }
            }
        }, 3L);
        Bukkit.getScheduler().runTaskLater(VLagger.MainThis, new Runnable() {
            @Override
            public void run() {
                Coord2D coord;
                for (int i = secondStage; i < total; i++) {
                    coord = fChunks.get(i);
                    world.getChunkAt(coord.getX(), coord.getZ());
                }
            }
        }, 5L);
        Bukkit.getScheduler().runTaskLater(VLagger.MainThis, new Runnable() {
            @Override
            public void run() {
                player.setInvulnerable(false);
                
                pending = true;
                player.teleport(to);
                pending = false;
            }
        }, 7L);
    }
    
    public static boolean canPreload(Location from, Location to, Player player) {
        if (from.getWorld() != to.getWorld()) return true;
        if (equals2D(from, to) || from.distance(to) < AzureAPI.viewDistanceBlock(player)) {
            return false;
        }

        return true;
    }
    
    public static boolean equals2D(Location from, Location to) {
        return from.getBlockX() == to.getBlockX() && from.getBlockZ() == to.getBlockZ();
    }
    
    public static List<Coord2D> collectPreloadChunks(Location loc, Player player) {
        val view = AzureAPI.viewDistanceBlock(player);
        int bX, bZ;
        bX = loc.getBlockX();
        bZ = loc.getBlockZ();
        int minX, minZ, maxX, maxZ;
        minX = bX - view;
        minZ = bZ - view;
        maxX = bX + view;
        maxZ = bZ + view;
        
        List<Coord2D> chunks = Lists.newArrayListWithExpectedSize(AzureAPI.viewDistanceChunk(player));
        int cx, cz;
        for (cx = minX; cx <= maxX; cx+=16) {
            for (cz = minZ; cz <= maxZ; cz+=16) {
                chunks.add(AzureAPI.wrapCoord2D(cx, cz));
            }
        }
        
        return chunks;
    }

}
