package com.mcml.space.optimize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.google.common.base.Predicate;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.PlayerList;

public class ChunkKeeper implements Listener {
    // TODO clear
    public static HashMap<Chunk, Integer> ChunkTimes = new HashMap<Chunk, Integer>();
    public static ArrayList<Chunk> ShouldKeepList = new ArrayList<Chunk>();

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (ShouldKeepList.contains(event.getChunk()) && ConfigOptimize.ChunkKeeperenable == true) {
            event.setCancelled(true);
        }
    }

    public static void ChunkKeeperofTask() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EscapeLag.PluginMain, new Runnable() {

            @Override
            public void run() {
                ChunkKeeper.ChunkTimes.clear();
            }
        }, 60 * 60 * 20, 60 * 60 * 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(EscapeLag.PluginMain, new Runnable() {

            @Override
            public void run() {
                if (ConfigOptimize.ChunkKeeperenable == true) {
                    PlayerList.forEach(new Predicate<Player>() {
                        @Override
                        public boolean apply(Player player) {
                            Chunk chunk = player.getLocation().getChunk();
                            if (ChunkTimes.get(chunk) == null) {
                                ChunkTimes.put(chunk, 1);
                            } else {
                                ChunkTimes.put(chunk, ChunkTimes.get(chunk) + 1);
                            }
                            if (ChunkTimes.get(chunk) > PlayerList.size() & ShouldKeepList.contains(chunk)==false) {
                                if(ShouldKeepList.size() > PlayerList.size()){
                                    ShouldKeepList.remove(0);
                                }
                                ShouldKeepList.add(chunk);
                            }
                            return true;
                        }
                    });
                }
            }
        }, 15 * 20, 15 * 20);
    }
}
