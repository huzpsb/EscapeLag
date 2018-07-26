package com.mcml.space.core;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.JoinReactor;
import com.mcml.space.util.QuitReactor;

import io.akarin.collect.set.player.PlayerSets;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class PlayerList implements Listener {
    private final static Set<Player> PLAYERS = PlayerSets.newUsernameInsensitiveBitSet();
    
    private final static List<JoinReactor> JOIN_REACTORS = Lists.newArrayList();
    private final static List<QuitReactor> QUIT_REACTORS = Lists.newArrayList();
    
    static {
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) PLAYERS.add(each);
        }
    }
    
    public static void bind(Plugin plugin) {
        assert plugin != null;
        Bukkit.getPluginManager().registerEvents(new PlayerList(), plugin);
        
        AzureAPI.log("玩家监控核心模块已启用");
    }
    
    public static void bind(JoinReactor reactor) {
        JOIN_REACTORS.add(reactor);
    }
    
    public static void bind(QuitReactor reactor) {
        QUIT_REACTORS.add(reactor);
    }
    
    public static void clear() {
        JOIN_REACTORS.clear();
        QUIT_REACTORS.clear();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if (!player.isOnline()) return;
        PLAYERS.add(player);
        
        if (!JOIN_REACTORS.isEmpty()) for (JoinReactor re : JOIN_REACTORS) re.react(evt);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent evt) {
        Player player = evt.getPlayer();
        PLAYERS.remove(player);
        
        if (!QUIT_REACTORS.isEmpty()) for (QuitReactor re : QUIT_REACTORS) re.react(evt);
    }
    
    public static boolean contains(Player player) {
        return PLAYERS.contains(player);
    }
    
    @SuppressWarnings("unlikely-arg-type")
    public static boolean contains(String username) {
        return PLAYERS.contains(username); // our library support this
    }
    
    public static boolean isEmpty() {
        return PLAYERS.isEmpty();
    }
    
    public static int size() {
        return PLAYERS.size();
    }
    
    public static void forEach(Predicate<Player> consumer) {
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) consumer.apply(each);
        }
    }
}
