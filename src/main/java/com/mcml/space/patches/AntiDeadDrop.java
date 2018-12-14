package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.VersionLevel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class AntiDeadDrop implements Listener{
    public static void init(){
        if(VersionLevel.isForge() == false && Patches.fixDupeDropItem){
            Bukkit.getPluginManager().registerEvents(new AntiDeadDrop(), EscapeLag.plugin);
            AzureAPI.log("反假死刷物品补丁已加载!");
        }
    }
    
    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(event.isCancelled() == false){
            Player player = event.getPlayer();
            if(PlayerList.contains(player) == false || player.isDead()){
                event.setCancelled(true);
            }
        }
    }
}
