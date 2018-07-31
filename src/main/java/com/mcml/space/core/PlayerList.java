package com.mcml.space.core;

import com.google.common.collect.Lists;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.JoinReactor;
import com.mcml.space.util.QuitReactor;
import io.akarin.collect.set.player.PlayerSets;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class PlayerList implements Listener {
    private final static Set<Player> PLAYERS = PlayerSets.newUsernameInsensitiveBitSet();
    
    private final static List<JoinReactor> JOIN_REACTORS = Lists.newArrayList();
    private final static List<QuitReactor> QUIT_REACTORS = Lists.newArrayList();
    
    public static void init(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new PlayerList(), plugin);
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) PLAYERS.add(each);
        }
        AzureAPI.log("核心模块 - 玩家列表已启用");
    }
    
    public static void bind(JoinReactor reactor) {
        JOIN_REACTORS.add(reactor);
    }
    
    public static void bind(QuitReactor reactor) {
        QUIT_REACTORS.add(reactor);
    }
    
    public static void clearReactors() {
        JOIN_REACTORS.clear();
        QUIT_REACTORS.clear();
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
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
        return PLAYERS.contains(username.toLowerCase(Locale.ROOT)); // our library support this
    }
    
    public static boolean isEmpty() {
        return PLAYERS.isEmpty();
    }
    
    public static int size() {
        return PLAYERS.size();
    }
    
    public static void forEach(Consumer<Player> consumer) {
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) consumer.accept(each);
        }
    }
}
