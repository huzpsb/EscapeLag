package com.mcml.space.optimize;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;

public class AntiRedstone implements Listener {
    private HashMap<Location,Integer> CheckedTimes = new HashMap<Location, Integer>();

    public AntiRedstone(){
        Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable(){
            @Override
            public void run(){
                CheckedTimes.clear();
            }
        }, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, 7));
    }

    @EventHandler
    public void CheckRedstone(BlockRedstoneEvent event){
    	if(ConfigOptimize.AntiRedstoneenable){
    		if(event.getOldCurrent() > event.getNewCurrent()){
                return;
            }
            final Block block = event.getBlock();
            Location loc = block.getLocation();
            if(CheckedTimes.get(loc) == null){
                CheckedTimes.put(loc, 0);
            }
            CheckedTimes.put(loc, CheckedTimes.get(loc) + 1);

            if(CheckedTimes.get(loc) > ConfigOptimize.AntiRedstoneTimes){
                if(ConfigOptimize.AntiRedstoneRemoveBlockList.contains(block.getType().name())){
					Bukkit.getScheduler().runTaskLater(EscapeLag.plugin,new Runnable(){
						public void run(){
							block.setType(Material.AIR);
						}
					},1);
                    String message = ConfigOptimize.AntiRedstoneMessage;
                    message = StringUtils.replace(message, "%location%", loc.toString());
                    AzureAPI.bc(message);
                }
            }
    	}
    }
}
