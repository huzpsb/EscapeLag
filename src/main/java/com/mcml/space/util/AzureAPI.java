package com.mcml.space.util;

import static com.mcml.space.util.VersionLevel.isPaper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author SotrForgotten, Vlvxingze
 */
public abstract class AzureAPI<K, V> {
    private static String loggerPrefix = "";
    private static final int bukkitVDChunk = (Bukkit.getViewDistance() * 2) ^ 2 + 1;
    private static final int bukkitVDBlock = Bukkit.getViewDistance() * 16;
    
    public static ChunkCoord wrapCoord(int chunkX, int chunkZ) {
        return new ChunkCoord(chunkX, chunkZ);
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class ChunkCoord {
        final int chunkX;
        final int chunkZ;
    }
    
    public static void bind(JavaPlugin bind) {
        ;
    }

    public static int viewDistance(final Player player) {
        return customViewDistance(player) ? player.getViewDistance() : Bukkit.getViewDistance();
    }

    public static int viewDistanceBlock(final Player player) {
        if (customViewDistance(player)) return player.getViewDistance() * 16;
        return bukkitVDBlock;
    }

    public static int viewDistanceChunk(final Player player) {
        if (customViewDistance(player)) return (player.getViewDistance() * 2) ^ 2 + 1;
        return bukkitVDChunk;
    }

    public static boolean customViewDistance(final Player player) {
        if (player == null || !isPaper()) return false;
        return player.getViewDistance() != Bukkit.getViewDistance();
    }

    public static String setPrefix(final String prefix) {
        loggerPrefix = prefix;
        return prefix;
    }

    public static void resetPrefix() {
        loggerPrefix = "";
    }
    
    public static void fatal(final String context, final JavaPlugin plugin) {
        fatal(loggerPrefix, context, plugin);
    }
    
    public static void fatal(final String prefix, final String context, final JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){
        	@Override
            public void run(){
        		Bukkit.getLogger().severe(prefix + context);
        	}
        }, 0L, TimeUnit.SECONDS.toMillis(30));
    }
    
    public static boolean isBlank(final String s) {
        return s.equals("");
    }
    
    public static void warn(final String context) {
        warn(loggerPrefix, context);
    }
    
    public static void warn(final String prefix, final String context) {
        if (isBlank(context)) return;
        Bukkit.getLogger().log(Level.WARNING, prefix + context);
    }
    
    public static void log(final String context) {
        log(loggerPrefix, context);
    }

    public static void log(final String prefix, final String context) {
        if (isBlank(context)) return;
        Bukkit.getConsoleSender().sendMessage(prefix + context);
    }

    public static void log(final CommandSender sender, final String context) {
        log(sender, loggerPrefix, context);
    }

    public static void log(final CommandSender sender, final String prefix, final String msg) {
        if (isBlank(msg)) return;
        sender.sendMessage(prefix + msg);
    }
    
    public static void bc(final String context) {
        bc(loggerPrefix, context);
    }
    
    public static void bc(final String prefix, final String context) {
        if (isBlank(context)) return;
        Bukkit.broadcastMessage(prefix + context);
    }

    public static long toTicks(TimeUnit unit, long duration) {
        return unit.toSeconds(duration) * 20;
    }

    public static Logger createLogger(final String prefix) {
        assert prefix != null;
        return new PrefixedLogger(prefix);
    }

    protected static class PrefixedLogger extends Logger {
        protected final String prefix;

        protected PrefixedLogger(final String prefix) {
            super(prefix, null);
            this.prefix = prefix;
            setParent(Logger.getGlobal());
            setLevel(Level.INFO);
        }

        @Override
        public void log(final LogRecord logRecord) {
            if (this.isLoggable(logRecord.getLevel())) Bukkit.getConsoleSender().sendMessage(prefix + logRecord.getMessage());
        }
    }

    public static <K, V> Coord<K, V> wrapCoord(K key, V value) {
        return new Coord<K, V>(key, value);
    }

    @Getter
    @AllArgsConstructor
    public static class Coord<K, V> {
        final K key;
        final V value;
    }
    
    public static <K, V, E> Coord3<K, V, E> wrapCoord(K key, V value, E extra) {
        return new Coord3<K, V, E>(key, value, extra);
    }
    
    @Getter
    @AllArgsConstructor
    public static class Coord3<K, V, E> {
        final K key;
        final V value;
        final E extra;
    }
    
    public static <E> Map<String, E> newCaseInsensitiveMap() {
        return newCaseInsensitiveMap(false);
    }
    
    public static <E> Map<String, E> newCaseInsensitiveMap(boolean concurrent) {
        return new CaseInsensitiveMap<E>(concurrent);
    }

    public static Set<String> newCaseInsensitiveSet() {
        return newCaseInsensitiveSet(false);
    }
    
    public static Set<String> newCaseInsensitiveSet(boolean concurrent) {
        return Sets.newSetFromMap(AzureAPI.<Boolean>newCaseInsensitiveMap(concurrent));
    }
    
    public static <E> List<E> matchElements(List<E> list, int start) {
        return matchElements(list, start, list.size() - 1);
    }
    
    /**
     * Returns elements between the start and end index, included the edge as well, collect to a list with capacity 'end - start + 1'
     */
    public static <E> List<E> matchElements(List<E> list, int start, int end) {
        List<E> t = Lists.newArrayListWithCapacity(end - start + 1);
        for (; start <= end; start++) {
            t.add(list.get(start));
        }
        return t;
    }
    
    public static String contactBetween(List<String> list, int start, char spilt) {
        return contactBetween(list, start, spilt);
    }
    
    public static String contactBetween(List<String> list, int start, String spilt) {
        return contactBetween(list, start, list.size() - 1, spilt);
    }
    
    public static String contactBetween(List<String> list, int start, int end, char spilt) {
        return contactBetween(list, start, end, spilt);
    }
    
    /**
     * Contacts strings between the start and end index, included the edge as well, then spilt with the given string
     */
    public static String contactBetween(List<String> list, int start, int end, String spilt) {
        String r = "";
        for (; start <= end; start++) {
            r = r.concat(list.get(start) + (start == end ? "" : spilt));
        }
        return r;
    }
    
    
    @SuppressWarnings("serial")
    public static class ChainArrayList<E> extends ArrayList<E> {
        public ChainArrayList<E> to(E e) {
            add(e);
            return this;
        }
    }
    
    public static boolean hasPerm(CommandSender sender, String perm) {
        return sender.isOp() || sender.hasPermission(perm);
    }
    
    public static boolean hasPerm(String username, String perm) {
        Player player = Bukkit.getPlayer(username);
        if (player == null) return false;
        return player.isOp() || player.hasPermission(perm);
    }
    
    public static boolean hasPerm(CommandSender sender, Permission perm) {
        return sender.isOp() || sender.hasPermission(perm);
    }
    
    public static FileConfiguration loadOrCreateFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void restartServer(final String message){
        if (VersionLevel.isSpigot()) {
            AzureAPI.log("开始以理由 " + message +"重启服务器...");
            PlayerList.forEach(new Predicate<Player>() {
                @Override
                public boolean apply(Player player) {
                    player.kickPlayer(loggerPrefix + message);
                    return true;
                }
            });
            org.spigotmc.RestartCommand.restart();
        } else {
            AzureAPI.log("请重启您的服务器");
            PlayerList.forEach(new Predicate<Player>() {
                @Override
                public boolean apply(Player player) {
                    player.kickPlayer(loggerPrefix + message);
                    return true;
                }
            });
            Bukkit.shutdown();
        }
    }
    
    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, false);
    }
    
    public static void playSound(Player player, Sound sound, boolean broadcast) {
        if (broadcast) {
            player.getWorld().playSound(player.getLocation(), sound, 1F, 1F);
        } else {
            player.playSound(player.getLocation(), sound, 1F, 1F);
        }
    }
    
    @SuppressWarnings("all")
    public static Object colorzine(Object o) {
        if (o instanceof String) {
            return StringUtils.replaceChars((String) o, '&', '§');
        }
        if (o instanceof List) {
            List list = (List) o;
            for (Object obj : list) {
                if (obj instanceof String) {
                    list.set(list.indexOf(obj), StringUtils.replaceChars((String) obj, '&', '§'));
                }
            }
            return list;
        }
        return o;
    }
}
