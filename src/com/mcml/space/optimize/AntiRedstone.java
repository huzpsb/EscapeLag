package com.mcml.space.optimize;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import com.mcml.space.config.ConfigMain;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.core.VLagger;
import com.mcml.space.util.AzureAPI;

public class AntiRedstone implements Listener {
	
	private HashMap<Location,Integer> CheckedTimes = new HashMap<Location, Integer>();

	public AntiRedstone(){
		Bukkit.getScheduler().runTaskTimer(VLagger.MainThis, new Runnable(){
			public void run(){
				CheckedTimes.clear();
			}
		}, 7 * 20, 7 * 20);
	}
	
	@EventHandler
	public void CheckRedstone(BlockRedstoneEvent event){
		Bukkit.broadcastMessage("红石监听器被触发！");
		Block block = event.getBlock();
		Location loc = block.getLocation();
		if(CheckedTimes.get(loc) == null){
			CheckedTimes.put(loc, 0);
		}
		CheckedTimes.put(loc, CheckedTimes.get(loc) + 1);
		Bukkit.broadcastMessage("当前阀值" + CheckedTimes.get(loc) + "配置限制阀值" + ConfigOptimize.AntiRedstoneTimes);
		if(CheckedTimes.get(loc) > ConfigOptimize.AntiRedstoneTimes){
			Bukkit.broadcastMessage("超出阀值！");
			if(ConfigOptimize.AntiRedstoneRemoveBlockList.contains(block.getType().name())){
				block.setType(Material.AIR);
				String message = ConfigOptimize.AntiRedstoneMessage;
				message = message.replaceAll("%location%", loc.toString());
				AzureAPI.bc(ConfigMain.PluginPrefix, message);
			}
		}
	}
}
