package com.mcml.space.core;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.util.Perms;
import lombok.Setter;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpgradeHelper implements Listener {

    @Setter
    private static boolean isKnown;
//    private static TextComponent component;

    public static void init(Plugin plugin) {
        if (Core.AutoUpdate) {
            return;
        }
//        if (VersionLevel.isHigherEquals(VersionLevel.Version.MINECRAFT_1_8_R1)) {
//            component = new TextComponent(Locale.isNative()
//                    ? "§3§lE§b§lL §b> §f输入 §3/el updateon §f开启自动更新, 永远保持高效运作! "
//                    : "§3§lE§b§lL §b> §fType §3/el updateon §fto enable auto-update, always keep running efficiently! ");
//            TextComponent knownOption = new TextComponent("§b".concat(Locale.isNative() ? "我知道了" : "Got it"));
//            knownOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/el iknown"));
//            component.addExtra(knownOption);
//            
//            AzureAPI.log(Locale.isNative() ? "核心模块 - 升级助手 已启动" : "Coremodule - UpgradeHelper has been enabled");
//        }
        Bukkit.getPluginManager().registerEvents(new UpgradeHelper(), plugin);
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
