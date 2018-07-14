package com.mcml.space.function;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;
import com.mcml.space.config.Features;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Perms;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.QuitReactor;

public class CensoredChat {
    public static void init(Plugin plugin) {
        if (Features.AntiSpamenable) Bukkit.getPluginManager().registerEvents(new SpamDetector(), plugin);
        
        if (Features.enableAntiDirty) {
            Bukkit.getPluginManager().registerEvents(new DirtyChatDetector(), plugin);
        }
    }
    
    private static class SpamDetector implements Listener, QuitReactor {
        private final static Map<String, Long> PLAYERS_CHAT_TIME = Maps.newHashMap();
        
        private SpamDetector() {
            PlayerList.bind(this);
            
            Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    PLAYERS_CHAT_TIME.clear();
                }
            }, 0L, AzureAPI.toTicks(TimeUnit.SECONDS, (int) Math.ceil(Features.AntiSpamPeriodPeriod) > 30 ? (int) Math.ceil(Features.AntiSpamPeriodPeriod) : 30));
        }
        
        @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
        public void checkSpam(AsyncPlayerChatEvent evt) {
            Player player = evt.getPlayer();
            if (Perms.has(player) || AzureAPI.hasPerm(player, "escapelag.bypass.spam")) return;
            
            long now = System.currentTimeMillis();
            if (isSpamming(player, now)) {
                evt.setCancelled(true);
                AzureAPI.log(player, Features.AntiSpamPeriodWarnMessage);
            }
        }
        
        @Override
        public void react(PlayerQuitEvent evt) {
            PLAYERS_CHAT_TIME.remove(evt.getPlayer().getName());
        }
        
        private static boolean isSpamming(Player player, long now) {
            Long last = PLAYERS_CHAT_TIME.get(player.getName());
            if (last == null) {
                PLAYERS_CHAT_TIME.put(player.getName(), System.currentTimeMillis());
                return false;
            }
            
            return System.currentTimeMillis() - last.longValue() <= Features.AntiSpamPeriodPeriod * 1000;
        }
    }
    
    private static class DirtyChatDetector implements Listener {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void checkChatDirty(AsyncPlayerChatEvent evt) {
            Player player = evt.getPlayer();
            if (Perms.has(player) || AzureAPI.hasPerm(player, "escapelag.bypass.dirty")) return;
            
            String message = evt.getMessage();
            for (Entry<String, Boolean> entry : Features.AntiSpamDirtyList.entrySet()) {
                if (handle(message, entry.getKey(), entry.getValue(), evt, player)) return;
            }
        }
        
        private static boolean handle(String message, String contain, boolean ignoreCase, Cancellable evt, Player player) {
            if (hasWhitelist(message, contain, ignoreCase)) {
                evt.setCancelled(true);
                AzureAPI.log(player, Features.AntiSpamDirtyWarnMessage);
                return true;
            }
            return false;
        }
        
        private static boolean hasWhitelist(String message, String contain, boolean ignoreCase) {
            message = ignoreCase ? message.toLowerCase() : message;
            int count = StringUtils.countMatches(message, ignoreCase ? contain.toLowerCase() : contain);
            if (count == 0) return true;
            for (String each : Features.AntiSpamDirtyWhitelist) {
                if (StringUtils.countMatches(message, ignoreCase ? each.toLowerCase() : each) >= count) return true;
            }
            return false;
        }
    }
}