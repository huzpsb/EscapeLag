package com.mcml.space.optimizations;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.google.common.collect.Lists;
import com.mcml.space.config.Optimizations;
import com.mcml.space.util.Utils;

public class UnloadClear implements Listener {
	public static ArrayList<Chunk> DeathChunk = Lists.newArrayList();

	@EventHandler
	public void ChunkloadClear(ChunkUnloadEvent event) {
		if (Optimizations.UnloadClearenable != true) {
			return;
		}
		Chunk chunk = event.getChunk();
		boolean noclearitemchunk = false;
		int dcs = DeathChunk.size();
		for (int i = 0; i < dcs; i++) {
			Chunk deathchunk = DeathChunk.get(i);
			if (Utils.isSameChunk(chunk, deathchunk)) {
				DeathChunk.remove(chunk);
				noclearitemchunk = true;
				break;
			}
		}
		Entity[] entities = chunk.getEntities();
		for (int i = 0; i < entities.length; i++) {
			Entity ent = entities[i];
			if (ent.getType() == EntityType.DROPPED_ITEM && noclearitemchunk == false && Optimizations.UnloadClearDROPPED_ITEMenable) {
				ent.remove();
			}
			if(Optimizations.UnloadCleartype.contains(ent.getType().name())||Optimizations.UnloadCleartype.contains("*")) {
				ent.remove();
			}
		}
	}

	@EventHandler
	public void DeathNoClear(PlayerDeathEvent event) {
		if (Optimizations.UnloadClearDROPPED_ITEMNoCleatDeath != true) {
			return;
		}
		Player player = event.getEntity();
		Chunk chunk = player.getLocation().getChunk();
		DeathChunk.add(chunk);
	}

	@EventHandler
	public void TeleportNoClear(PlayerTeleportEvent event) {
		if (Optimizations.UnloadClearDROPPED_ITEMNoClearTeleport != true) {
			return;
		}
		Player player = event.getPlayer();
		Chunk chunk = player.getLocation().getChunk();
		DeathChunk.add(chunk);
	}
}
