package com.mcml.space.patch;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import com.mcml.space.config.ConfigPatch;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public class AntiDupeDropItem implements Listener {
	
	// @author jiongjionger,Vlele
	
	public static void init(){
		if(VersionLevel.get() == Version.MINECRAFT_1_7_R4 && VersionLevel.isForge()){
			AzureAPI.log("警告！您的服务器是 1.7.10 的Mod 服务端，因此关闭了假死刷物品的补丁!");
		}else{
			Bukkit.getPluginManager().registerEvents(new AntiDupeDropItem(), EscapeLag.plugin);
		}
	}
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent evt) {
        if(ConfigPatch.fixDupeDropItem){
            Player player = evt.getPlayer();
            if (PlayerList.contains(player) && player.isDead()) evt.setCancelled(true);
        }
    }
}
