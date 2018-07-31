package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class AntiFakeDeath {

    public static void init(Plugin plugin) {
        if (!Patches.noFakedeath) {
            return;
        }

        Bukkit.getScheduler().runTaskTimer(plugin, AntiFakeDeath::clearUndeadPlayers, 0L, 7 * 20);
    }

    public static void clearUndeadPlayers() {
        PlayerList.forEach(player -> {
            if (player.getHealth() <= 0 && !player.isDead()) {
                player.setHealth(0.0);
                player.kickPlayer(Patches.messageFakedeath);
            }
        });
    }
}
