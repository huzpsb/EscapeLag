package com.mcml.space.util;

import com.google.common.collect.Lists;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.PlayerList;
import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.lang.management.ThreadInfo;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.WatchdogThread;

/**
 * @author SotrForgotten, Vlvxingze
 */
public abstract class AzureAPI {

    /**
     * Cached prefix for every message
     */
    private static String loggerPrefix;

    /**
     * Server view distance (squared, both sides) in chunks
     */
    private static final int squaredViewDistanceChunk = (Bukkit.getViewDistance() * 2) ^ 2 + 1;

    /**
     * Server view distance (direct, single side) in blocks
     */
    private static final int viewDistanceBlock = Bukkit.getViewDistance() * 16;

    /**
     * Cached server thread instance
     */
    private static Thread serverThread;

    /**
     * Get cached server thread instance, lookup it if not have a cache yet
     *
     * @return the main thread
     */
    @Nonnull
    public static Thread serverThread() {
        if (serverThread != null) {
            return serverThread;
        }
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("Server thread")) {
                return (serverThread = t);
            }
        }
        throw new AssertionError(Locale.isNative() ? "找不到服务器主线程!" : "NO SERVER THREAD!");
    }

    /**
     * Coord stores chunk x and z as primitive int
     */
    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class ChunkCoord {
        public ChunkCoord(int cx,int cz){
            chunkX = cx;
            chunkZ = cz;
        }

        final int chunkX;
        final int chunkZ;
    }

    /**
     * Creates a chunk coord by chunk x and z (performs >> 4 by a block pos)
     *
     * @param chunkX
     * @param chunkZ
     * @return chunk coord
     */
    public static ChunkCoord wrapCoord(int chunkX, int chunkZ) {
        return new ChunkCoord(chunkX, chunkZ);
    }

    /**
     * Get the standard view distance from player, server value if current
     * server not supporting custom value
     *
     * @param player
     * @return
     */
    public static int viewDistance(Player player) {
        return customViewDistance(player) ? player.getViewDistance() : Bukkit.getViewDistance();
    }

    /**
     * Get view distance (direct, single side) in block from player, server
     * value if current server not supporting custom value
     *
     * @param player
     * @return
     */
    public static int viewDistanceBlock(Player player) {
        if (customViewDistance(player)) {
            return player.getViewDistance() * 16;
        }
        return viewDistanceBlock;
    }

    /**
     * Get view distance (squared, both sidess) in chunk from player, server
     * value if current server not supporting custom value
     *
     * @param player
     * @return has
     */
    public static int viewDistanceChunk(Player player) {
        if (customViewDistance(player)) {
            return (player.getViewDistance() * 2) ^ 2 + 1;
        }
        return squaredViewDistanceChunk;
    }

    /**
     * Check if the player has a custom view distance from player, false if
     * current server not supporting custom value
     *
     * @param player
     * @return has
     */
    public static boolean customViewDistance(Player player) {
        if (player == null || !VersionLevel.canAccessPaperViewDistanceApi()) {
            return false;
        }
        return player.getViewDistance() != Bukkit.getViewDistance();
    }

    /**
     * Set given prefix, the prefix will be appended before every message, set
     * as null to reset.
     *
     * @param prefix
     * @return colornized prefix
     */
    public static String setPrefix(String prefix) {
        loggerPrefix = (prefix = StringUtils.replaceChars(prefix, '&', '§'));
        return prefix;
    }

    /**
     * Reset the prefix, i.e set to null.
     */
    public static void resetPrefix() {
        loggerPrefix = null;
    }

    /**
     * Concat the prefix with the context in the fastest way (according from
     * benchmarks)
     *
     * @param prefix
     * @param context
     * @return the prefixed context
     */
    public static String prefix(String prefix, String context) {
        return prefix == null ? context : prefix.concat(context);
    }

    /**
     * Send a severe level message to console with global prefix and disable the
     * plugin
     *
     * @param context
     * @param plugin
     */
    public static void fatal(String context, JavaPlugin plugin) {
        fatal(loggerPrefix, context, plugin);
    }

    /**
     * Send a severe level message to console with given prefix and disable the
     * plugin
     *
     * @param prefix
     * @param context
     * @param plugin
     */
    public static void fatal(String prefix, String context, JavaPlugin plugin) {
        Bukkit.getLogger().severe(prefix(prefix, context));
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    /**
     * Send a warning level message to console with global prefix
     *
     * @param context
     */
    public static void warn(String context) {
        warn(loggerPrefix, context);
    }

    /**
     * Send a warning level message to console with given prefix
     *
     * @param context
     */
    public static void warn(String prefix, String context) {
        Bukkit.getLogger().log(Level.WARNING, prefix(prefix, context));
    }

    /**
     * Send a info level message to console with global prefix
     *
     * @param context
     */
    public static void log(String context) {
        log(loggerPrefix, context);
    }

    /**
     * Send a info level message to console with given prefix
     *
     * @param prefix
     * @param context
     */
    public static void log(String prefix, String context) {
        Bukkit.getConsoleSender().sendMessage(prefix(prefix, context));
    }

    /**
     * Send a message to the sender with global prefix
     *
     * @param sender
     * @param context
     */
    public static void log(CommandSender sender, String context) {
        log(sender, loggerPrefix, context);
    }

    /**
     * Send a message to the sender with given prefix
     *
     * @param sender
     * @param prefix
     * @param msg
     */
    public static void log(CommandSender sender, String prefix, String context) {
        sender.sendMessage(prefix(prefix, context));
    }

    /**
     * Broadcast a message with global prefix
     *
     * @param context
     */
    public static void bc(String context) {
        bc(loggerPrefix, context);
    }

    /**
     * Broadcast a message with global prefix and placing the placeholders
     *
     * @param context
     */
    public static void bc(String context, String placeholder, String value) {
        bc(loggerPrefix, context, placeholder, value);
    }

    /**
     * Broadcast a message with given prefix and placing the placeholders
     *
     * @param context
     */
    public static void bc(String prefix, String context, String placeholder, String value) {
        bc(prefix, StringUtils.replace(context, placeholder, value));
    }

    /**
     * Broadcast a message with given prefix
     *
     * @param context
     */
    public static void bc(String prefix, String context) {
        Bukkit.broadcastMessage(prefix(prefix, context));
    }

    /**
     * Converts a time to standard (20 per seconds) ticks
     *
     * @param unit time unit
     * @param duration
     * @return in ticks
     */
    public static long toTicks(TimeUnit unit, long duration) {
        return unit.toSeconds(duration) * 20;
    }

    /**
     * Creates a coord that wraps the given objects
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V> Coord<K, V> wrapCoord(K key, V value) {
        return new Coord<K, V>(key, value);
    }

    /**
     * Coord stores key and value data
     *
     * @param <K> first object
     * @param <V> second object
     */
    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Coord<K, V> {

        final K key;
        final V value;
    }

    /**
     * Creates a coord that wraps the given objects
     *
     * @param key
     * @param value
     * @param extra
     * @return
     */
    public static <K, V, E> Coord3<K, V, E> wrapCoord(K key, V value, E extra) {
        return new Coord3<K, V, E>(key, value, extra);
    }

    /**
     * Coord stores key and value, extra data
     *
     * @param <K> first object
     * @param <V> second object
     * @param <E> third object
     */
    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Coord3<K, V, E> {

        final K key;
        final V value;
        final E extra;
    }

    /**
     * Returns elements from the start, the edge included, collects to a list
     * with immutable capacity. Respects the standard list index.
     *
     * @param list
     * @param start
     * @param end
     * @return
     */
    public static <E> List<E> matchElements(List<E> list, int start) {
        return matchElements(list, start, list.size() - 1);
    }

    /**
     * Returns elements between the start and end index, the edge included,
     * collects to a list with immutable capacity. Respects the standard list
     * index.
     *
     * @param list
     * @param start
     * @param end
     * @return
     */
    public static <E> List<E> matchElements(List<E> list, int start, int end) {
        List<E> t = Lists.newArrayListWithCapacity(end - start + 1);
        for (; start <= end; start++) {
            t.add(list.get(start));
        }
        return t;
    }

    /**
     * Contacts strings from the start, included the edge as well. Respects the
     * standard list index.
     */
    public static String concatsBetween(List<String> list, int start, char spilt) {
        return concatsBetween(list, start, spilt);
    }

    /**
     * Contacts strings from the start, included the edge as well, then spilt by
     * the given string. Respects the standard list index.
     */
    public static String concatsBetween(List<String> list, int start, String spilt) {
        return concatsBetween(list, start, list.size() - 1, spilt);
    }

    /**
     * Contacts strings between the start and end index, included the edge as
     * well, then spilt by the given char. Respects the standard list index.
     */
    public static String concatsBetween(List<String> list, int start, int end, char spilt) {
        return concatsBetween(list, start, end, spilt);
    }

    /**
     * Contacts strings between the start and end index, included the edge as
     * well, then spilt by the given string. Respects the standard list index.
     */
    public static String concatsBetween(List<String> list, int start, int end, String spilt) {
        String concated = "";
        for (; start <= end; start++) {
            concated = concated.concat(list.get(start).concat(start == end ? "" : spilt));
        }
        return concated;
    }

    /**
     * Check whether the sender has specified permission, ignore ops
     *
     * @param sender
     * @param perm
     * @return
     */
    public static boolean hasPerm(CommandSender sender, String perm) {
        return sender.hasPermission(perm);
    }

    /**
     * Check whether the player has specified permission, false if cannot find,
     * ignore ops
     *
     * @param username
     * @param perm
     * @return
     */
    public static boolean hasPerm(String username, String perm) {
        Player player = Bukkit.getPlayer(username);
        return player == null ? false : player.hasPermission(perm);
    }

    /**
     * Check whether the sender has specified permission, ignore ops
     *
     * @param sender
     * @param perm
     * @return
     */
    public static boolean hasPerm(CommandSender sender, Permission perm) {
        return sender.hasPermission(perm);
    }

    /**
     * Try to load a config, creates an empty one if not exist
     *
     * @param file
     * @return
     */
    public static FileConfiguration loadOrCreateConfiguration(File file) {
        try {
            file = createDirectories(file);
            file.createNewFile();
        } catch (IOException ex) {
            AzureAPI.fatal(Locale.isNative()
                    ? "无法创建文件 '" + file.getPath() + "', 已锁定?"
                    : "Cannot create file '" + file.getPath() + "', blocked?",
                    EscapeLag.plugin);
            ex.printStackTrace();
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Fixes a file that name contains path, transfer them to its path
     *
     * @param file
     * @return
     */
    public static File fixesFilePath(File file) {
        return StringUtils.contains(file.getName(), "/") || StringUtils.contains(file.getName(), "\\") || StringUtils.contains(file.getName(), separator)
                ? new File(
                        StringUtils.substringBeforeLast(fixesPathSeparator(file.getPath()), separator) + separator + StringUtils.substringBeforeLast(fixesPathSeparator(file.getName()), separator), // fixed patch
                        StringUtils.substringAfterLast(fixesPathSeparator(file.getName()), separator)) // fixed name
                : file;
    }

    /**
     * Replaces '/' to '\'
     *
     * @param path
     * @return
     */
    public static String fixesPathSeparator(String path) {
        return StringUtils.replace(path, "/", separator);
    }

    /**
     * Try to create the parent directories for the file
     *
     * @param file
     * @return
     */
    public static File createDirectories(File file) {
        file = fixesFilePath(file);
        new File(StringUtils.substringBeforeLast(fixesPathSeparator(file.getPath()), separator)).mkdirs();
        return file;
    }

    /**
     * Try to restart server, false if not supports and stopping server if
     * failed
     *
     * @param message
     * @return Whether restarted smoothly
     */
    /**
     * Try to restart server, false if not supports and stopping server if force
     *
     * @param message
     * @return Whether restarted smoothly
     */
    public static void restartServer(String message) {
        PlayerList.forEach(player -> player.kickPlayer(prefix(loggerPrefix, message)));
        Bukkit.shutdown();
    }

    /**
     * Plays a private sound at the player location
     *
     * @param player
     * @param sound
     */
    public static void playSound(Player player, Sound sound) {
        playSound(player, sound, false);
    }

    /**
     * Plays a public sound at the player location
     *
     * @param player
     * @param sound
     */
    public static void playSound(Player player, Sound sound, boolean broadcast) {
        if (broadcast) {
            player.getWorld().playSound(player.getLocation(), sound, 10F, 1F);
        } else {
            player.playSound(player.getLocation(), sound, 10F, 1F);
        }
    }

    /**
     * Try to colornize the element, no-op if not supports
     *
     * @param element
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <E> E colorzine(E element, Class<E> type) {
        if (element instanceof String) {
            return (E) StringUtils.replaceChars((String) element, '&', '§');
        }

        if (element instanceof String[]) {
            String[] array = (String[]) element;
            for (int i = 0; i < array.length; i++) {
                array[i] = StringUtils.replaceChars(array[i], '&', '§');
            }
            return (E) array;
        }

        if (element instanceof List) {
            List<String> c = (List<String>) element;
            for (String each : c) {
                c.set(c.indexOf(each), StringUtils.replaceChars(each, '&', '§'));
            }
            return (E) c;
        }

        if (element instanceof Set) {
            Set<String> c = (Set<String>) element;
            for (String each : c) {
                c.add(StringUtils.replaceChars(each, '&', '§'));
            }
            return (E) c;
        }

        return element;
    }

    /*
     * The following codes are from Paper and released under The MIT License (MIT) license.
     * Related contributor(s): Daniel Ennis <aikar@aikar.co>
     * 
     * The MIT License (MIT)
     * =====================
     * 
     * Permission is hereby granted, free of charge, to any person
     * obtaining a copy of this software and associated documentation
     * files (the “Software”), to deal in the Software without
     * restriction, including without limitation the rights to use,
     * copy, modify, merge, publish, distribute, sublicense, and/or sell
     * copies of the Software, and to permit persons to whom the
     * Software is furnished to do so, subject to the following
     * conditions:
     * 
     * The above copyright notice and this permission notice shall be
     * included in all copies or substantial portions of the Software.
     * 
     * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
     * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
     * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
     * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
     * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
     * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
     * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
     * OTHER DEALINGS IN THE SOFTWARE.
     */
    private static final Pattern SPACE = Pattern.compile(" ");
    private static final Pattern NOT_NUMERIC = Pattern.compile("[^-\\d.]");

    public static int getSeconds(String str) {
        str = SPACE.matcher(str).replaceAll("");
        final char unit = str.charAt(str.length() - 1);
        str = NOT_NUMERIC.matcher(str).replaceAll("");
        double num;
        try {
            num = Double.parseDouble(str);
        } catch (Exception e) {
            num = 0D;
        }
        switch (unit) {
            case 'd':
                num *= (double) 60 * 60 * 24;
                break;
            case 'h':
                num *= (double) 60 * 60;
                break;
            case 'm':
                num *= (double) 60;
                break;
            default:
            case 's':
                break;
        }
        return (int) num;
    }

    public static String timeSummary(int seconds) {
        String time = "";

        if (seconds > 60 * 60 * 24) {
            time += TimeUnit.SECONDS.toDays(seconds) + "d";
            seconds %= 60 * 60 * 24;
        }

        if (seconds > 60 * 60) {
            time += TimeUnit.SECONDS.toHours(seconds) + "h";
            seconds %= 60 * 60;
        }

        if (seconds > 0) {
            time += TimeUnit.SECONDS.toMinutes(seconds) + "m";
        }
        return time;
    }

    /*
     * 为了开源，直接日WatchDog
     */
    public static void dumpThread(ThreadInfo thread, Logger logger) {
        if (VersionLevel.isSpigot()) {
            try {
                Class<WatchdogThread> wdt = WatchdogThread.class;
                Method dumpthreadmd = wdt.getDeclaredMethod("dumpThread", ThreadInfo.class, Logger.class);
                dumpthreadmd.setAccessible(true);
                dumpthreadmd.invoke(null, thread, logger);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(AzureAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
