package com.mcml.space.util;

import static com.mcml.space.util.VersionLevel.hasViewDistanceApi;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.Sets;

/**
 * @author SotrForgotten
 */
public class AzureAPI {
    private static String loggerPrefix;
    private static final int bukkitVDChunk = (Bukkit.getViewDistance() * 2) ^ 2 + 1;
    private static final int bukkitVDBlock = Bukkit.getViewDistance() * 16;

    private static final class LazyAPI {
        private static final AzureAPI api = new AzureAPI();
    }

    private AzureAPI() {
        assert Bukkit.getServer() != null;
        assert LazyAPI.api == null;
    }

    public static final AzureAPI getAPI() {
        return LazyAPI.api;
    }

    public static int viewDistance(final Player player) {
        return hasViewDistanceApi() ? player.getViewDistance() : Bukkit.getViewDistance();
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
        if (!hasViewDistanceApi()) return false;
        return Bukkit.getViewDistance() == player.getViewDistance();
    }

    public static String setPrefix(final String prefix) {
        loggerPrefix = prefix;
        return prefix;
    }

    public static void resetPrefix() {
        loggerPrefix = null;
    }

    public static void log(final String prefix, final String context) {
        Bukkit.getConsoleSender().sendMessage(prefix + context);
    }

    public static void log(final String context) {
        Bukkit.getConsoleSender().sendMessage(loggerPrefix == null ? context : loggerPrefix + context);
    }

    public static void log(final CommandSender sender, final String context) {
        sender.sendMessage(loggerPrefix == null ? context : loggerPrefix + context);
    }

    public static void log(final CommandSender sender, final String prefix, final String msg) {
        sender.sendMessage(prefix + msg);
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

    public static Coord2D wrapCoord2D(int x, int z) {
        return new Coord2D(x, z);
    }

    public static class Coord2D {
        final int x;
        final int z;

        public Coord2D(int x2d, int z2d) {
            x = x2d;
            z = z2d;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }

    public static <E> Map<String, E> newCaseInsensitiveMap() {
        return new CaseInsensitiveMap<E>();
    }

    public static Set<String> newCaseInsensitiveSet() {
        return Sets.newSetFromMap(new CaseInsensitiveMap<Boolean>());
    }
}
