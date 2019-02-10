package com.mcml.space.optimizations;

import com.mcml.space.util.AzureAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class NoSpawnChunk{
    public static void init(){
        for(World world:Bukkit.getWorlds()){
            world.setKeepSpawnInMemory(false);
        }
        AzureAPI.log("子模块 - 区块释放 已启动");
    }
}
