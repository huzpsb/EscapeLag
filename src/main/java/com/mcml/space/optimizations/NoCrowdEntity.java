package com.mcml.space.optimizations;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.mcml.space.config.Optimizations;

public class NoCrowdEntity implements Listener {

	@EventHandler
	public void CheckCrowd(ChunkLoadEvent evt) {
		if (Optimizations.NoCrowdedEntityenable) {
			Chunk chunk = evt.getChunk();
			Entity[] entities = chunk.getEntities();

			for (Entity e : entities) {
				EntityType type = e.getType();
				int count = 0;
				if (Optimizations.NoCrowdedEntityTypeList.contains("*")
						|| Optimizations.NoCrowdedEntityTypeList.contains(type.name())) {
					count++;
					if (count > Optimizations.NoCrowdedEntityPerChunkLimit && e.getType() != EntityType.PLAYER) {
						e.remove();
					}
				}
			}
		}
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		if (Optimizations.NoCrowdedEntityenable) {
			Chunk chunk = event.getEntity().getLocation().getChunk();
			Entity[] entities = chunk.getEntities();

			for (Entity e : entities) {
				EntityType type = e.getType();
				int count = 0;
				if (Optimizations.NoCrowdedEntityTypeList.contains("*")
						|| Optimizations.NoCrowdedEntityTypeList.contains(type.name())) {
					count++;
					if (count > Optimizations.NoCrowdedEntityPerChunkLimit && e.getType() != EntityType.PLAYER) {
						e.remove();
					}
				}
			}
		}
	}
}
