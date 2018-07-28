package com.mcml.space.core;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerList implements Listener{
    public static List<Player> Players = new ArrayList();
    
    public static void init(){
        for(World world:Bukkit.getWorlds()){
            Players.addAll(world.getPlayers());
        }
        Bukkit.getPluginManager().registerEvents(new PlayerList(), EscapeLag.plugin);
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Players.add(event.getPlayer());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Players.remove(event.getPlayer());
    }
}
