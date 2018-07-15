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
import com.mcml.space.core.EscapeLag;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author SotrForgotten, Vlvxingze
 */
public abstract class AzureAPI {
    private static String loggerPrefix = "";
    private static final int bukkitVDChunk = (Bukkit.getViewDistance() * 2) ^ 2 + 1;
    private static final int bukkitVDBlock = Bukkit.getViewDistance() * 16;
    
    public static ChunkCoord wrapCoord(int chunkX, int chunkZ) {
        return new ChunkCoord(chunkX, chunkZ);
    }
    
    private static Thread serverThread;
    
    public static Thread getMainThread() {
        if (serverThread != null) return serverThread;
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("Server thread")) return (serverThread = t);
        }
        throw new AssertionError("Cannot find main thread!");
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class ChunkCoord {
        final int chunkX;
        final int chunkZ;
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
        Bukkit.getLogger().severe(prefix + context);
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
    
    public static boolean isBlank(final String s) {
        return s.equals("");
    }
    
    public static void warn(final String context) {
        warn(loggerPrefix, context);
    }
    
    public static void warn(final String prefix, final String context) {
        Bukkit.getLogger().log(Level.WARNING, prefix + context);
    }
    
    public static void log(final String context) {
        log(loggerPrefix, context);
    }

    public static void log(final String prefix, final String context) {
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
    
    public static void bc(final String context, final String placeholder, final String placevalue) {
        bc(loggerPrefix, StringUtils.replace(context, placeholder, placevalue));
    }
    
    public static void bc(final String prefix, final String context) {
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
        return sender.hasPermission(perm);
    }
    
    public static boolean hasPerm(String username, String perm) {
        Player player = Bukkit.getPlayer(username);
        if (player == null) return false;
        return player.hasPermission(perm);
    }
    
    public static boolean hasPerm(CommandSender sender, Permission perm) {
        return sender.hasPermission(perm);
    }
    
    public static FileConfiguration loadOrCreateConfiguration(File file) {
        try {
            file = createDirectories(file);
            file.createNewFile();
        } catch (IOException ex) {
            AzureAPI.fatal("Cannot create file '" + file.getPath() + "', blocked?", EscapeLag.plugin);
            ex.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }
    
    public static File fixesFilePath(File file) {
         return StringUtils.contains(file.getName(), "/") || StringUtils.contains(file.getName(), "\\") ?
                 new File(
                         StringUtils.substringBeforeLast(fixesPath(file.getPath()), "\\") + "\\" + StringUtils.substringBeforeLast(fixesPath(file.getName()), "\\"), // fixed patch
                         StringUtils.substringAfterLast(fixesPath(file.getName()), "\\")) // fixed name
                : file;
    }
    
    public static String fixesPath(String path) {
        return StringUtils.replace(path, "/", "\\");
   }
    
    public static File createDirectories(File file) {
        file = fixesFilePath(file);
        new File(StringUtils.substringBeforeLast(fixesPath(file.getPath()), "\\")).mkdirs();
        return file;
    }
    
    public static boolean restartServer(final String message){
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
            return true;
        } else {
            AzureAPI.log("请重启您的服务器");
            return false;
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
    
    @SuppressWarnings("unchecked")
    public static <E> E colorzine(E element, Class<E> type) {
        if (element instanceof String) return (E) ((String) element).replace('&', '§');
        
        if (element instanceof String[]) {
            String[] array = (String[]) element;
            for (int i = 0; i < array.length; i++) array[i] = array[i].replace('&', '§');
            return (E) array;
        }
        
        if (element instanceof List) {
            List<String> c = (List<String>) element;
            for (String each : c) c.set(c.indexOf(each), each.replace('&', '§'));
            return (E) c;
        }
        
        if (element instanceof Set) {
            Set<String> c = (Set<String>) element;
            for (String each : c) c.add(each.replace('&', '§'));
            return (E) c;
        }
        
        return element;
    }
}
