package com.mcml.space.optimize;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.plugin.Plugin;

import com.mcml.space.core.EscapeLag;

public class WorldSpawnLimiter implements Listener {
    public static void init(Plugin plugin) {
        
    }
    
    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        World world = event.getWorld();
        FileConfiguration config = EscapeLag.configurations.get(EscapeLag.CONFIG_OPTIMIZE).getValue();
        if (config.getBoolean("WorldSpawnLimitor." + world.getName() + ".enable")) {
            world.setMonsterSpawnLimit(config.getInt("WorldSpawnLimitor." + world.getName() + ".PerChunkMonsters"));
            world.setAnimalSpawnLimit(config.getInt("WorldSpawnLimitor." + world.getName() + ".PerChunkAnimals"));
            world.setAmbientSpawnLimit(config.getInt("WorldSpawnLimitor." + world.getName() + ".PerChunkAmbient"));
            EscapeLag.plugin.getLogger().info("已为世界 " + world.getName() + " 设定了生物刷新速率~");
        }
    }
}
