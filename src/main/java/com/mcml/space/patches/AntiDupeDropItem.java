package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.plugin.Plugin;

public class AntiDupeDropItem implements Listener {
	
	// @author jiongjionger,Vlele
	
	public static void init(Plugin plugin) {
	    if (!Patches.fixDupeDropItem) return;
	    
		if(VersionLevel.equals(Version.MINECRAFT_1_7_R4) && VersionLevel.isForge()){
			AzureAPI.log("提示！您的服务器是 1.7.10 的Mod 服务端，因此关闭了假死刷物品的补丁!");
		}else{
			Bukkit.getPluginManager().registerEvents(new AntiDupeDropItem(), EscapeLag.plugin);
		}
	}
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrop(PlayerDropItemEvent evt) {
        Player player = evt.getPlayer();
        if (PlayerList.contains(player) && player.isDead()) evt.setCancelled(true);
    }
}
