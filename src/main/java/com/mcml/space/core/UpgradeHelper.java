package com.mcml.space.core;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.Perms;
import com.mcml.space.util.VersionLevel;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpgradeHelper implements Listener {

    @Setter
    private static boolean isKnown;

    public static void init(Plugin plugin) {
        if (Core.AutoUpdate) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new UpgradeHelper(), plugin);
        AzureAPI.log(Locale.isNative() ? "核心模块 - 升级助手 已启动" : "Coremodule - UpgradeHelper has been enabled");
    }

    @EventHandler
    public void react(PlayerJoinEvent evt) {
        if (isKnown) {
            return;
        }
        Player player = evt.getPlayer();
        if (Perms.has(player)) {
            player.sendMessage("§3§lE§b§lL §b> §f输入 §3/el updateon §f开启自动更新, 永远保持高效运作! ");
        }
    }
}
