package com.mcml.space.monitor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcml.space.config.ConfigFunction;

public class MonitorEnabler implements Listener{
	private boolean hasEnabled = false;
	
	@EventHandler
	public void JoinEnableMonitor(PlayerJoinEvent event){
		if(hasEnabled == false){
			if(ConfigFunction.Monitorenable){
				MonitorUtils.enable();
			}
			hasEnabled = true;
		}
	}
}
