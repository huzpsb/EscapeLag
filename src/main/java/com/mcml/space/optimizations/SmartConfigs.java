package com.mcml.space.optimizations;

import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import com.mcml.space.config.Optimizations;
import com.mcml.space.core.EscapeLag;
import lombok.SneakyThrows;

public class SmartConfigs { // Not ready for use now
    public static void init(Plugin plugin, boolean force) {
        if(!force && !Optimizations.AutoSetenable) return;
        
        File backupFolder = new File("backup_configurations");
        if (backupFolder.isDirectory()) backupFolder.delete();
        backupFolder.mkdir();
        
        File bukkitFile = new File("bukkit.yml");
        if (bukkitFile.exists()) configsBukkit(bukkitFile);
        
        File spigotFile = new File("spigot.yml");
        if (spigotFile.exists()) configsSpigot(spigotFile);
        
        Optimizations.AutoSaveenable = false;
        EscapeLag.plugin.setupConfig(EscapeLag.CONFIG_OPTIMIZE, Optimizations.class);
    }
    
    @SneakyThrows
    private static FileConfiguration loadAndBackup(File file) {
        File backup = new File("backup_configurations/" + file.getName());
        backup.createNewFile();
        
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.save(backup);
        return configuration;
    }
    
    private static void configsBukkit(File bukkitFile) {
        FileConfiguration bukkitConfig = loadAndBackup(bukkitFile);
        
        long heapMb = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        if (heapMb <= 6144) {
            if (bukkitConfig.getInt("chunk-gc.period-in-ticks") == 600) {
                long targetGCPeriod = (heapMb / 6144) * 36000 /* 30 MINS */;
                bukkitConfig.set("chunk-gc.period-in-ticks", targetGCPeriod < 6000 /* 5 MINS */ ? 6000 : targetGCPeriod);
            }
            
            if (bukkitConfig.getInt("chunk-gc.load-threshold") == 0) {
                long targetThreshold = heapMb / 10;
                bukkitConfig.set("chunk-gc.load-threshold", targetThreshold < 200 ? 200 : targetThreshold);
            }
            
            if (bukkitConfig.getInt("ticks-per.autosave") == 6000) {
                long targetAutoSaveTicks = (6144 / heapMb) * 6000 /* 5 MINS */;
                bukkitConfig.set("ticks-per.animal-spawns", targetAutoSaveTicks > 18000 /* 15 MINS */ ? 18000 : targetAutoSaveTicks);
            }
        }
        
        if (heapMb <= 4096) {
            if (bukkitConfig.getInt("ticks-per.animal-spawns") == 400) {
                long targetAnimalsTicks = heapMb / 20;
                bukkitConfig.set("ticks-per.animal-spawns", targetAnimalsTicks < 200 ? 200 : targetAnimalsTicks);
            }
        }
    }
    
    private static void configsSpigot(File spigotFile) {
        @SuppressWarnings("unused")
        FileConfiguration spigotConfig = loadAndBackup(spigotFile);
        //...
    }
}