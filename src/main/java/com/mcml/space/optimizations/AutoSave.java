package com.mcml.space.optimizations;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.mcml.space.config.Optimizations;
import com.mcml.space.core.EscapeLag;

public class AutoSave implements Listener {
    // TODO benchmark!
    private HashMap<Player, Integer> TaskId = new HashMap<Player, Integer>();
    private static HashMap<Player, Chunk> PlayerInChunkMap = new HashMap<Player, Chunk>();
    private static HashMap<Player, Chunk> PlayerClickedMap = new HashMap<Player, Chunk>();

    @EventHandler
    public void JoinTaskGiver(PlayerJoinEvent e) {
        if (Optimizations.AutoSaveenable == false) {
            return;
        }
        final Player p = e.getPlayer();
        TaskId.put(p, Bukkit.getScheduler().scheduleSyncRepeatingTask(EscapeLag.plugin, new Runnable() {

            @Override
            public void run() {
                Chunk chunk = p.getLocation().getChunk();
                Chunk LastChunk = PlayerInChunkMap.get(p);
                if (LastChunk != chunk) {
                    if (LastChunk == null) {
                        PlayerInChunkMap.put(p, chunk);
                        return;
                    }
                    if (PlayerClickedMap.containsValue(LastChunk)) {
                        return;
                    }
                    World world = LastChunk.getWorld();
                    if (LastChunk.isLoaded() == true) {
                    	world.unloadChunk(LastChunk.getX(), LastChunk.getZ(), true);
                        LastChunk.load();
                    } else {
                        LastChunk.load();
                        world.unloadChunk(LastChunk.getX(), LastChunk.getZ(), true);
                    }
                }
                PlayerInChunkMap.put(p, chunk);
                p.saveData();
            }
        }, Optimizations.AutoSaveInterval * 20, Optimizations.AutoSaveInterval * 20));
    }

    @EventHandler
    public void ClickBypassList(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) {
            return;
        }
        if (Optimizations.AutoSaveenable == false) {
            return;
        }
        Player p = e.getPlayer();
        PlayerClickedMap.put(p, e.getClickedBlock().getChunk());
    }

    @EventHandler
    public void QuitCancelled(PlayerQuitEvent e) {
        if (Optimizations.AutoSaveenable == false) {
            return;
        }
        Player p = e.getPlayer();
        if (TaskId.get(p) != null) {
            Bukkit.getScheduler().cancelTask(TaskId.get(p));
            TaskId.remove(p);
        }
    }
}
