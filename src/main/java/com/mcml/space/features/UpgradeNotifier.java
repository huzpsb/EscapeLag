package com.mcml.space.features;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.util.Locale;
import com.mcml.space.util.Perms;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UpgradeNotifier implements Listener {
    @Setter private static boolean isKnown;
    private static TextComponent component;
    
    public static void init(Plugin plugin) {
        component = new TextComponent(Locale.isNative() ?
                "§3§lE§b§lL §b> §f输入 §3/el updateon §f开启自动更新, 永远保持高效运作! "
                :
                "§3§lE§b§lL §b> §fType §3/el updateon §fto enable auto-update, always keep running efficiently! ");
        TextComponent knownOption = new TextComponent("§b§n".concat(Locale.isNative() ? "我知道了" : "Got it"));
        knownOption.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/el iknown"));
        component.addExtra(knownOption);
        
        Bukkit.getPluginManager().registerEvents(new UpgradeNotifier(), plugin);
    }
    
    @EventHandler
    public void react(PlayerJoinEvent evt) {
        if(isKnown || Core.AutoUpdate) return;
        
        Player player = evt.getPlayer();
        if (Perms.has(player)) player.sendMessage(component);
    }
}
