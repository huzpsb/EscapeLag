package com.mcml.space.optimizations;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import com.mcml.space.config.Optimizes;

public class NoCrowdEntity implements Listener {

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent evt) {
        if (Optimizes.NoCrowdedEntityenable) {
            Chunk chunk = evt.getChunk();
            Entity[] entities = chunk.getEntities();

            for (Entity e : entities) {
                EntityType type = e.getType();
                int count = 0;
                if (Optimizes.NoCrowdedEntityTypeList.contains("*")
                        || Optimizes.NoCrowdedEntityTypeList.contains(type.name())) {
                    count++;
                    if (count > Optimizes.NoCrowdedEntityPerChunkLimit && e.getType() != EntityType.PLAYER) {
                        e.remove();
                    }
                }
            }
        }
    }
}
