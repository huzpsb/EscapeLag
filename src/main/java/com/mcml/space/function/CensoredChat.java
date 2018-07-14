package com.mcml.space.function;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
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
            
            if (Features.enableAntiDirtyCheckSign) Bukkit.getPluginManager().registerEvents(new DirtySignDetector(), plugin);
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
    
    private static class DirtySignDetector implements Listener {
        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void checkSignDirty(final SignChangeEvent event) {
            final Player player = event.getPlayer();
            if (Perms.has(player) || AzureAPI.hasPerm(player, "escapelag.bypass.dirty")) return;
            
            Bukkit.getScheduler().runTaskAsynchronously(EscapeLag.plugin, new Runnable() {
                @Override
                public void run() {
                    int curIndex = 0;
                    final Map<Integer, String> replacedIndexs = Maps.newHashMap();
                    for (String line : event.getLines()) {
                        for (String each : Features.AntiSpamDirtyList) {
                            handle(line, each, false, replacedIndexs, curIndex++);
                        }
                        for (String each : Features.AntiSpamDirtyListIgnoreCase) {
                            handle(line, each, true, replacedIndexs, curIndex++);
                        }
                    }
                    
                    Bukkit.getScheduler().runTask(EscapeLag.plugin, new Runnable() {
                        @Override
                        public void run() {
                            Block signBlock = event.getBlock();
                            Material type = signBlock.getType();
                            if (type == Material.SIGN || type == Material.WALL_SIGN || type == Material.SIGN_POST) {
                                Sign sign = (Sign) signBlock;
                                for (Entry<Integer, String> entry : replacedIndexs.entrySet()) sign.setLine(entry.getKey(), entry.getValue());
                            }
                        }
                    });
                    AzureAPI.log(player, Features.AntiSpamDirtyWarnMessage);
                }
            });
        }
        
        private static void handle(String message, String contain, boolean ignoreCase, Map<Integer, String> replacedIndexs, int curIndex) {
            String replaced = ignoreCase ? message.replaceAll("(?i)" + contain, "") : StringUtils.replace(message, contain, "");
            if (!message.equalsIgnoreCase(replaced)) replacedIndexs.put(curIndex, replaced);
        }
    }
    
    private static class DirtyChatDetector implements Listener {
        @EventHandler(ignoreCancelled = true)
        public void checkChatDirty(AsyncPlayerChatEvent evt) {
            Player player = evt.getPlayer();
            if (Perms.has(player) || AzureAPI.hasPerm(player, "escapelag.bypass.dirty")) return;
            
            String message = evt.getMessage();
            for (String each : Features.AntiSpamDirtyList) {
                if (handle(message, each, false, evt, player)) return;
            }
            for (String each : Features.AntiSpamDirtyListIgnoreCase) {
                if (handle(message, each, true, evt, player)) return;
            }
        }
        
        private static boolean handle(String message, String contain, Boolean ignoreCase, Cancellable evt, Player player) {
            if (ignoreCase ? StringUtils.containsIgnoreCase(message, contain) : StringUtils.contains(message, contain)) {
                evt.setCancelled(true);
                AzureAPI.log(player, Features.AntiSpamDirtyWarnMessage);
                return true;
            }
            return false;
        }
    }
    
}
