package com.mcml.space.optimize;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.google.common.base.Predicate;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;

public class EntityClear implements Runnable{

	@Override
	public void run() {
		if(ConfigOptimize.EntityClearenable){
			int count = 0;
			List<World> worlds = Bukkit.getWorlds();
			final List<LivingEntity> allents = new ArrayList<LivingEntity>();
			int ws = worlds.size();
			for(int i = 0;i<ws;i++){
				World world = worlds.get(i);
				List<LivingEntity> lents = world.getLivingEntities();
				int ls = lents.size();
				for(int ii = 0;ii<ls;ii++){
					LivingEntity le = lents.get(ii);
					if(ConfigOptimize.EntityClearEntityType.contains("*") ||ConfigOptimize.EntityClearEntityType.contains(le.getType().name())){
						allents.add(le);
						count = count + lents.size();
					}
				}
			}
			if(count > ConfigOptimize.EntityClearLimitCount){
	            PlayerList.forEach(new Predicate<Player>() {
	                @Override
	                public boolean apply(Player player) {
	                    List<Entity> nents = player.getNearbyEntities(10, 10, 10);
	                    int ls = nents.size();
	                    for(int ii = 0;ii<ls;ii++){
	                        Entity le = nents.get(ii);
	                        if(allents.contains(le)){
	                            allents.remove(le);
	                        }
	                    }
	                    return true;
	                }
	            });
				int aes = allents.size();
				AzureAPI.bc(ConfigOptimize.EntityClearClearMessage);
				for(int ii = 0;ii<aes;ii++){
					allents.get(ii).remove();
				}
			}
		}
	}
}
