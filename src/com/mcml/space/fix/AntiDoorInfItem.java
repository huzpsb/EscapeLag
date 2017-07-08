package com.mcml.space.fix;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;

import com.mcml.space.core.VLagger;

public class AntiDoorInfItem
        implements Listener {

    @EventHandler
    public void PlaceCheckDoor(BlockPlaceEvent e) {
		if(VLagger.AntiDoorInfItemenable == true){
			if(e.getBlock().getType().name().contains("DOOR")){
				Player p = e.getPlayer();
				Chunk chunk = p.getLocation().getChunk();
				Entity[] entities = chunk.getEntities();
				for(int i=0;i<entities.length;i++){
					Entity ent = entities[i];
					if(ent.getType() == EntityType.DROPPED_ITEM){
						Item item = (Item)ent;
						if(item.getItemStack().getType() == Material.SUGAR_CANE|item.getItemStack().getType() == Material.CACTUS){
							ent.remove();
						}
					}
				}
			}
		}
    }
}
/**
@author jiongjionger
部分源码来自 https://github.com/jiongjionger/NeverLag
*/
