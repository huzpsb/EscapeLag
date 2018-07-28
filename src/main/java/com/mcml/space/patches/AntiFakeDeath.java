package com.mcml.space.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import com.mcml.space.config.Patches;
import com.mcml.space.core.PlayerList;

public class AntiFakeDeath {

    public static void init(Plugin plugin) {
        if (!Patches.noFakedeath) {
            return;
        }

        Bukkit.getScheduler().runTaskTimer(plugin, AntiFakeDeath::clearUndeadPlayers, 0L, 7 * 20);
    }

    public static void clearUndeadPlayers() {
        for (Player player : PlayerList.Players) {
            if (player.getHealth() <= 0 && !player.isDead()) {
                player.setHealth(0.0);
                player.kickPlayer(Patches.messageFakedeath);
            }
        }
    }
}
