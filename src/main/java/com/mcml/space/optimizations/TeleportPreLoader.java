package com.mcml.space.optimizations;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import com.mcml.space.config.Optimizes;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI.ChunkCoord;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.Utils;

import static com.mcml.space.util.VersionLevel.modernApi;
import static org.bukkit.Material.*;
import org.bukkit.event.EventPriority;

public class TeleportPreLoader implements Listener {

    private boolean isPreLoading;
    private int nowteleportid = 0;
    private HashMap<Integer, Integer> nowint = new HashMap<Integer, Integer>();

    @EventHandler(priority = EventPriority.LOWEST)
    public void TeleportLoader(final PlayerTeleportEvent event) {
        if (Optimizes.TeleportPreLoaderenable == true) {
            final Player player = event.getPlayer();
            if (shouldReload(event.getFrom(), event.getTo(), player) == false) {
                return;
            }
            if (player.getVehicle() != null) {
                return;
            }
            if (event.getFrom().getBlock().getType() == ENDER_PORTAL) {
                return;
            }
            nowteleportid++;
            if (isPreLoading == false) {
                event.setCancelled(true);
                final int thistpid = nowteleportid;
                final List<ChunkCoord> chunks = Utils.getShouldUseChunks(event.getTo());
                final int cs = chunks.size();
                if (nowint.get(thistpid) == null) {
                    nowint.put(thistpid, 0);
                }
                final World world = event.getTo().getWorld();

                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 1);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 2);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 3);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 4);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 5);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 6);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 7);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 8);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    nowint.put(thistpid, nowint.get(thistpid) + cs / 10);
                    for (int i = nowint.get(thistpid) - cs / 10; i < nowint.get(thistpid); i++) {
                        ChunkCoord coord = chunks.get(i);
                        world.loadChunk(coord.getChunkX(), coord.getChunkZ());
                    }
                }, 9);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    isPreLoading = true;
                    player.teleport(event.getTo());
                    isPreLoading = false;
                    nowint.remove(thistpid);
                }, 10);
            }
        }
    }

    private boolean shouldReload(Location from, Location to, Player player) {
        if (from.getWorld() != to.getWorld()) {
            return true;
        }
        Vector fvec = from.toVector();
        Vector tvec = to.toVector();
        double distance = fvec.distance(tvec);
        if (distance < 6) {
            return false;
        }
        return true;
    }
}
