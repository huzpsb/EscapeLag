package com.mcml.space.optimize;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.mcml.space.config.ConfigOptimize;

public class NoCrowdEntity implements Listener {

	@EventHandler
	public void CheckCrowd(ChunkLoadEvent evt) {
		if (ConfigOptimize.NoCrowdedEntityenable) {
			Chunk chunk = evt.getChunk();
			Entity[] entities = chunk.getEntities();

			for (Entity e : entities) {
				EntityType type = e.getType();
				int count = 0;
				if (ConfigOptimize.NoCrowdedEntityTypeList.contains("*")
						|| ConfigOptimize.NoCrowdedEntityTypeList.contains(type.name())) {
					count++;
					if (count > ConfigOptimize.NoCrowdedEntityPerChunkLimit && e.getType() != EntityType.PLAYER) {
						e.remove();
					}
				}
			}
		}
	}

	@EventHandler
	public void onSpawn(CreatureSpawnEvent event) {
		if (ConfigOptimize.NoCrowdedEntityenable) {
			Chunk chunk = event.getEntity().getLocation().getChunk();
			Entity[] entities = chunk.getEntities();

			for (Entity e : entities) {
				EntityType type = e.getType();
				int count = 0;
				if (ConfigOptimize.NoCrowdedEntityTypeList.contains("*")
						|| ConfigOptimize.NoCrowdedEntityTypeList.contains(type.name())) {
					count++;
					if (count > ConfigOptimize.NoCrowdedEntityPerChunkLimit && e.getType() != EntityType.PLAYER) {
						e.remove();
					}
				}
			}
		}
	}
}
