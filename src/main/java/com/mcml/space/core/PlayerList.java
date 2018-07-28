package com.mcml.space.core;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayerList implements Listener{
    
    public static void init(){
    }
    
    public static List<Player> getPlayerList(){
        List<Player> players = new ArrayList();
        for(World world:Bukkit.getWorlds()){
            players.addAll(world.getPlayers());
        }
        return players;
    }
}
