package com.mcml.space.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.mcml.space.config.ConfigAntiBug;
import com.mcml.space.core.VLagger;

public class AntiCrashSign implements Listener {

    @EventHandler
    public void SignCheckChange(SignChangeEvent event) {
        if (VLagger.AntiCrashSignenable == true) {
            Player player = event.getPlayer();
            String[] lines =event.getLines();
            int ll = lines.length;
            for(int i = 0;i<ll;i++){
                String line = lines[i];
                if(line.length() >= 127){
                    event.setCancelled(true);
                    if(ConfigAntiBug.AntiCrashSignWarnMessage.equalsIgnoreCase("none") == false){
                        player.sendMessage(VLagger.PluginPrefix + ConfigAntiBug.AntiCrashSignWarnMessage);
                    }
                }
            }
        }
    }
}