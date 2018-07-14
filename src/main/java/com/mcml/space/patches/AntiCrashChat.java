package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Patches;
import com.mcml.space.util.AzureAPI;

public class AntiCrashChat implements Listener{
    private boolean HasEss;

    public AntiCrashChat(){
        Plugin ess = Bukkit.getPluginManager().getPlugin("Essentials");
        if(ess != null){
            HasEss = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void ChatCheckCrash(AsyncPlayerChatEvent event){
        if (!Patches.noCrashChat) return;
        
        Player player = event.getPlayer();
        String message = event.getMessage();
        if(message.contains("İ")){
            event.setCancelled(true);
            AzureAPI.log(player, Patches.AntiCrashChatSpecialStringWarnMessage);
        }
        if(HasEss == true){
            if(message.contains("&")){
                event.setCancelled(true);
                AzureAPI.log(player, Patches.AntiCrashChatSpecialStringWarnMessage);
            }
        }
    }
}
// TODO confirm details