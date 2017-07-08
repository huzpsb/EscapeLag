package com.mcml.space.optimize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.mcml.space.core.VLagger;
import com.mcml.space.util.Utils;

public class ChunkKeeper
        implements Listener {

    public static HashMap<Chunk, Integer> ChunkTimes = new HashMap<Chunk, Integer>();
    public static ArrayList<Chunk> ShouldKeepList = new ArrayList<Chunk>();

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        if (ShouldKeepList.contains(event.getChunk()) & VLagger.ChunkKeeperenable == true) {
            event.setCancelled(true);
        }
    }

    public static void ChunkKeeperofTask() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(VLagger.MainThis, new Runnable() {

				@Override
				public void run() {
					ChunkKeeper.ChunkTimes.clear();
				}
			}, 60 * 60 * 20, 60 * 60 * 20);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VLagger.MainThis, new Runnable() {

            @Override
            public void run() {
                if (VLagger.ChunkKeeperenable == true) {
                    ArrayList<Player> onlinePlayers = Utils.getonlinePlayers();
                    Iterator<? extends Player> players = onlinePlayers.iterator();
                    while (players.hasNext()) {
                        Player player = players.next();
                        Chunk chunk = player.getLocation().getChunk();
                        if (ChunkTimes.get(chunk) == null) {
                            ChunkTimes.put(chunk, 1);
                        } else {
                            ChunkTimes.put(chunk, ChunkTimes.get(chunk) + 1);
                        }
                        if (ChunkTimes.get(chunk) > Utils.getonlinePlayers().size() & ShouldKeepList.contains(chunk)==false) {
                            if(ShouldKeepList.size() > Utils.getonlinePlayers().size()){
								ShouldKeepList.remove(0);
                            }
							ShouldKeepList.add(chunk);
                        }
                    }
                }
            }
        }, 15 * 20, 15 * 20);
    }
}
