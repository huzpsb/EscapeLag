package com.mcml.space.features;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mcml.space.config.Core;
import com.mcml.space.util.JoinReactor;
import com.mcml.space.util.Perms;

public class UpgradeNotifier implements JoinReactor {
    @Override
    public void react(PlayerJoinEvent evt) {
        if(Core.AutoUpdate) return;
        
        Player player = evt.getPlayer();
        if(Perms.has(player)){
            player.sendMessage("§a§l[EscapeLag]§e提示:§b输入/el updateon 来开启自动更新，永远保持你的服务器运行高效！");
            player.sendMessage("§a§l[EscapeLag]§eTip: §benter /el updateon to open auto update, always maintain your server running efficiently!");
        }
    }
    
}
