package com.mcml.space.optimize;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.Utils;

public class ItemClear implements Listener {

	public static ArrayList<Chunk> DeathChunk = new ArrayList<Chunk>();

	@EventHandler
	public void ChunkloadClear(ChunkUnloadEvent event) {
		if (ConfigOptimize.ClearItemenable != true) {
			return;
		}
		Chunk chunk = event.getChunk();
		int dcs = DeathChunk.size();
		boolean noclear = false;
		List<Chunk> thenremove = new ArrayList<Chunk>();
		for (int i = 0; i < dcs; i++) {
			Chunk donotchunk = DeathChunk.get(i);
			if (Utils.isSameChunk(chunk, donotchunk)) {
				noclear = true;
				thenremove.add(chunk);
			}
		}
		thenremove.removeAll(thenremove);
		if (noclear == false) {
			Entity[] entities = chunk.getEntities();
			for (int i = 0; i < entities.length; i++) {
				Entity ent = entities[i];
				if (ent.getType() == EntityType.DROPPED_ITEM) {
					if (ConfigOptimize.ClearItemNoClearItemType.contains(((Item) ent).getType().name()) == false) {
						ent.remove();
					}
				}
			}
		}
	}

	@EventHandler
	public void DeathNoClear(PlayerDeathEvent event) {
		if (ConfigOptimize.ClearItemNoCleatDeath != true) {
			return;
		}
		Player player = event.getEntity();
		Chunk chunk = player.getLocation().getChunk();
		List<Chunk> chunks = Utils.getnearby9chunks(chunk);
		DeathChunk.addAll(chunks);
	}

	@EventHandler
	public void TeleportNoClear(PlayerTeleportEvent event) {
		if (ConfigOptimize.ClearItemNoClearTeleport != true) {
			return;
		}
		Player player = event.getPlayer();
		Chunk chunk = player.getLocation().getChunk();
		List<Chunk> chunks = Utils.getnearby9chunks(chunk);
		DeathChunk.addAll(chunks);
	}
}
