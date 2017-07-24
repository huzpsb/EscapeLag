package com.mcml.space.optimize;

import org.bukkit.Bukkit;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.config.ConfigPluginMain;
import com.mcml.space.core.VLagger;

public class HeapShut implements Runnable {
    // TODO why not restart?
    @Override
    public void run() {
        if (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() > Runtime.getRuntime().maxMemory() / 100 * ConfigOptimize.HeapShutPercent) {
            if (ConfigOptimize.HeapShutenable == true) {
                if(ConfigOptimize.HeapShutWarnMessage.equalsIgnoreCase("none") == false){
                    Bukkit.broadcastMessage(ConfigPluginMain.PluginPrefix + ConfigOptimize.HeapShutWarnMessage);
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(VLagger.MainThis, new Runnable() {

                    @Override
                    public void run() {
                        Bukkit.shutdown();
                    }
                }, ConfigOptimize.HeapShutWaitingTime * 20);
            }
        }
    }
}
