package com.mcml.space.patches;

import com.mcml.space.config.Patches;
import com.mcml.space.core.PlayerList;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.Plugin;

public class DupeLoginPatch implements Listener, PluginExtends {

    public static void init(Plugin plugin) {
        if (!Patches.fixDupeOnline) {
            return;
        }

        Bukkit.getPluginManager().registerEvents(new DupeLoginPatch(), plugin);
        AzureAPI.log("多重在线修复模块已启用");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(AsyncPlayerPreLoginEvent evt) {
        for (Player player : PlayerList.getPlayerList()) {
            if (player.getName().equals(evt.getName())) {
                evt.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                evt.setKickMessage(Patches.messageKickDupeOnline);
            }
        }
    }
}
