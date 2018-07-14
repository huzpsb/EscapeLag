package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.google.common.base.Predicate;
import com.mcml.space.config.Patches;
import com.mcml.space.util.PlayerList;

public class AntiFakeDeath {
    public static void init(Plugin plugin) {
        if (!Patches.noFakedeath) return;
        
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                clearUndeadPlayers();
            }
        }, 0L, 7 * 20);
    }
    
    public static void clearUndeadPlayers() {
        PlayerList.forEach(new Predicate<Player>() {
            @Override
            public boolean apply(Player player) {
                if(player.getHealth() <= 0 && !player.isDead()){
                    player.setHealth(0.0);
                    player.kickPlayer(Patches.messageFakedeath);
                }
                return true;
            }
        });
    }
}
