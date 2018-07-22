package com.mcml.space.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.mcml.space.command.EscapeLagCommand;
import com.mcml.space.command.LocalizedHelper;
import com.mcml.space.config.Features;
import com.mcml.space.config.Core;
import com.mcml.space.config.Optimizes;
import com.mcml.space.config.OptimizesChunk;
import com.mcml.space.config.Patches;
import com.mcml.space.config.PatchesDupeFixes;
import com.mcml.space.features.AutoRespawn;
import com.mcml.space.features.CensoredChat;
import com.mcml.space.features.ExplosionController;
import com.mcml.space.features.FarmProtection;
import com.mcml.space.features.SpawnerController;
import com.mcml.space.features.UpgradeNotifier;
import com.mcml.space.optimizations.AutoSave;
import com.mcml.space.optimizations.DelayedChunkKeeper;
import com.mcml.space.optimizations.NoStyxChunks;
import com.mcml.space.optimizations.EmptyRestart;
import com.mcml.space.optimizations.FireSpreadSlacker;
import com.mcml.space.optimizations.NoCrowdEntity;
import com.mcml.space.optimizations.OverloadRestart;
import com.mcml.space.optimizations.RedstoneSlacker;
import com.mcml.space.optimizations.TeleportPreLoader;
import com.mcml.space.optimizations.TickSleep;
import com.mcml.space.optimizations.TimerGarbageCollect;
import com.mcml.space.optimizations.UnloadClear;
import com.mcml.space.optimizations.WaterFlowLimitor;
import com.mcml.space.patches.AntiBedExplode;
import com.mcml.space.patches.AntiCrashSign;
import com.mcml.space.patches.AntiDupeDropItem;
import com.mcml.space.patches.AntiFakeDeath;
import com.mcml.space.patches.AntiLongStringCrash;
import com.mcml.space.patches.AutoRecipePatch;
import com.mcml.space.patches.BonemealDupePatch;
import com.mcml.space.patches.CalculationAbusePatch;
import com.mcml.space.patches.CancelledPlacementPatch;
import com.mcml.space.patches.CheatBookBlocker;
import com.mcml.space.patches.ContainerPortalPatch;
import com.mcml.space.patches.DupeLoginPatch;
import com.mcml.space.patches.NegativeItemPatch;
import com.mcml.space.patches.NetherHopperDupePatch;
import com.mcml.space.patches.RailsMachine;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.Coord;
import com.mcml.space.util.Configurable;
import com.mcml.space.util.Perms;
import com.mcml.space.util.VersionLevel;

import lombok.SneakyThrows;

public class EscapeLag extends JavaPlugin {
    public static EscapeLag plugin;
    
    // Core
    public final static String CONFIG_CORE = "PluginMainConfig.yml";
    
    // Features
    public final static String CONFIG_FEATURES = "DoEventConfig.yml";
    
    // Pathches
    public final static String CONFIG_PATCHES = "AntiBugConfig.yml";
    public final static String CONFIG_PATCH_DUPE_FIXES = "patches/dupe_fixes.yml"; // The separator will be automatically fixed
    
    // Optimizations
    public final static String CONFIG_OPTIMIZES = "ClearLagConfig.yml";
    public final static String CONFIG_OPTIMIZE_DUPE_FIXES = "optimizes/chunks.yml";
    
    public final static Map<String, Coord<File, FileConfiguration>> configurations = Maps.newHashMap(); // File name as key
    
    public final static String GLOBAL_PERMS = "escapelag.admin";
    
    @Override
    public void onEnable() {
        plugin = this;
        
        setupConfigs();
        
        // Non-task only once binds
        Perms.bind(GLOBAL_PERMS);
        EscapeLag.AutoSetServer(false);
        
        AzureAPI.log("EscapeLag —— 新一代的优化/稳定插件");
        AzureAPI.log("~(@^_^@)~ 玩的开心！~");
        
        AzureAPI.log("Version " + getDescription().getVersion() + " is ready for installation \n");
        AzureAPI.log("Server: " + Bukkit.getServer().getVersion());
        AzureAPI.log("Bukkit: " + Bukkit.getServer().getBukkitVersion());
        AzureAPI.log("Level: " + VersionLevel.get() + "\n");
        
        AzureAPI.log("Setup modules..");
        bindCoreModules();
        
        AzureAPI.log("EscapeLag has been installed successfully!");
        AzureAPI.log("乐乐感谢您的使用——有建议务必反馈，QQ1207223090");
        AzureAPI.log("您可以在插件根目录找到本插件的说明文档 说明文档.txt");
        List<String> devs = getDescription().getAuthors();
        AzureAPI.log("|||" + devs.get(0) + "/EscapeLag 合作作品.|||");
        AzureAPI.log("|||" + AzureAPI.concatsBetween(devs, 1, ", ") + " 合作开发.|||");
        AzureAPI.log("§a您正在使用EscapeLag构建号 " + Core.internalVersion);
        
        TimerGarbageCollect.collectGarbage();
    }
    
    @Override
    public void onDisable() {
        clearModules(); // Not every disable is server stopping
        
        AzureAPI.log("Plugin has been disabled.");
        AzureAPI.log("Thanks for using!");
    }
    
    public void clearModules() {
        AzureAPI.log("Uninstall modules..");
        Ticker.cancelNotifyTask();
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTasks(this);
        
        TimerGarbageCollect.collectGarbage();
    }
    
    public void bindCoreModules() {
        // Clears pervious
        PlayerList.clear();
        
        // Sudmodules
        PlayerList.bind(new UpgradeNotifier());
        
        // Reentrant binds
        // Core
        Ticker.init(this);
        Network.init(this);
        PlayerList.bind(this);
        
        // Features
        CensoredChat.init(this);
        ExplosionController.init(this);
        SpawnerController.init(this);
        FarmProtection.init(this);
        AutoRespawn.init(this);
        
        Bukkit.getPluginManager().registerEvents(new NoCrowdEntity(), this);
        
        // Pathces
        ContainerPortalPatch.init(this);
        NetherHopperDupePatch.init(this);
        NegativeItemPatch.init(this);
        RailsMachine.init(this);
        DupeLoginPatch.init(this);
        AntiDupeDropItem.init(this);
        BonemealDupePatch.init(this);
        CheatBookBlocker.init(this);
        CalculationAbusePatch.init(this);
        AntiFakeDeath.init(this);
        
        Bukkit.getPluginManager().registerEvents(new AntiCrashSign(), this);
        Bukkit.getPluginManager().registerEvents(new CancelledPlacementPatch(), this);
        Bukkit.getPluginManager().registerEvents(new AntiBedExplode(), this);
        Bukkit.getPluginManager().registerEvents(new AntiLongStringCrash(), this);
        if (canAccessPackets()) AutoRecipePatch.init(this);
        
        // Optimizations
        TickSleep.init(this);
        EmptyRestart.init(this);
        OverloadRestart.init(this);
        FireSpreadSlacker.init(this);
        RedstoneSlacker.init(this);
        DelayedChunkKeeper.init(this);
        TimerGarbageCollect.init(this);
        NoStyxChunks.init(this);
        
        Bukkit.getPluginManager().registerEvents(new UnloadClear(), this);
        Bukkit.getPluginManager().registerEvents(new AutoSave(), this);
        Bukkit.getPluginManager().registerEvents(new WaterFlowLimitor(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportPreLoader(), this);
    }
    
    public static boolean canAccessPackets() {
        return Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
    }
    
    public static File getPluginFile() {
        return EscapeLag.plugin.getFile();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("EL")) {
            return EscapeLagCommand.processCommand(sender, cmd, label, args);
        }
        return false;
    }
    
    private Coord<File, FileConfiguration> configsFile(String name) {
        File configFile = new File(this.getDataFolder(), name);
        Coord<File, FileConfiguration> coord = AzureAPI.<File, FileConfiguration>wrapCoord(configFile, AzureAPI.loadOrCreateConfiguration(configFile));
        configurations.put(name, coord);
        return coord;
    }
    
    public void setupConfigs() {
        String locale = "english";
        if (StringUtils.startsWithIgnoreCase(Core.lang, "zh_")) locale = "中文";
        EscapeLag.plugin.saveResource("documents" + File.separator + "Guide-" + locale + ".txt", true);
        
        // Core
        setupConfig(CONFIG_CORE, Core.class);
        
        // Features
        setupConfig(CONFIG_FEATURES, Features.class);
        
        // Patches
        setupConfig(CONFIG_PATCHES, Patches.class);
        setupConfig(CONFIG_PATCH_DUPE_FIXES, PatchesDupeFixes.class);
        
        // Optimizations
        setupConfig(CONFIG_OPTIMIZES, Optimizes.class);
        setupConfig(CONFIG_OPTIMIZE_DUPE_FIXES, OptimizesChunk.class);
    }
    
    public boolean setupConfig(String configIdentifier, Class<? extends Configurable> provider) {
        configurations.remove(configIdentifier);
        Coord<File, FileConfiguration> configCoord = configsFile(configIdentifier);
        
        try {
            Configurable.restoreNodes(configCoord, provider);
        } catch (IllegalArgumentException | IllegalAccessException illegal) {
            AzureAPI.fatal("Cannot setup configuration '" + configCoord.getKey().getName() + "', wrong format?", this);
            illegal.printStackTrace();
            return false;
        } catch (IOException io) {
            AzureAPI.fatal("Cannot load configuration '" + configCoord.getKey().getName() + "', file blocked?", this);
            io.printStackTrace();
            return false;
        }
        
        if (configIdentifier.equals(CONFIG_CORE)) {
            AzureAPI.setPrefix(Core.PluginPrefix + ChatColor.RESET + " > ");
            LocalizedHelper.init();
        }
        return true;
    }
    
    @SneakyThrows
    public static void AutoSetServer(boolean force) {
        if (!force && !Optimizes.AutoSetenable) return;
        
        long heapmb = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        File BukkitFile = new File("bukkit.yml");
        if (BukkitFile.exists()) {
            FileConfiguration bukkit = AzureAPI.loadOrCreateConfiguration(BukkitFile);
            File backupBukkitFile = new File("backup_bukkit.yml");
            if (backupBukkitFile.exists() == false) {
                backupBukkitFile.createNewFile();
                bukkit.save(backupBukkitFile);
                if (heapmb <= 6000) {
                    bukkit.set("chunk-gc.period-in-ticks", 300);
                } else {
                    bukkit.set("chunk-gc.period-in-ticks", 500);
                }
                bukkit.set("chunk-gc.load-threshold", 400);
                if (heapmb <= 4000) {
                    bukkit.set("ticks-per.monster-spawns", 2);
                }
                bukkit.set("EscapeLag.Changed", "如果Config的AutoSet开启，该参数会被改变。");
                bukkit.save(BukkitFile);
            }
        }
        File SpigotFile = new File("spigot.yml");
        if (SpigotFile.exists()) {
            FileConfiguration spigot = AzureAPI.loadOrCreateConfiguration(SpigotFile);
            File backupSpigotFile = new File("backup_spigot.yml");
            if (backupSpigotFile.exists() == false) {
                backupSpigotFile.createNewFile();
                spigot.save(backupSpigotFile);
                if (heapmb <= 2000) {
                    spigot.set("settings.save-user-cache-on-stop-only", true);
                }
                if (heapmb >= 6000) {
                    spigot.set("settings.user-cache-size", 5000);
                }
                if (heapmb >= 8000) {
                    spigot.set("world-settings.default.view-distance", 4);
                } else if (heapmb >= 4000) {
                    spigot.set("world-settings.default.view-distance", 3);
                } else {
                    spigot.set("world-settings.default.view-distance", 2);
                }
                if (heapmb <= 4000) {
                    spigot.set("world-settings.default.chunks-per-tick", 150);
                } else {
                    spigot.set("world-settings.default.chunks-per-tick", 350);
                }
                if (heapmb <= 4000) {
                    spigot.set("world-settings.default.max-tick-time.tile", 10);
                    spigot.set("world-settings.default.max-tick-time.entity", 20);
                } else {
                    spigot.set("world-settings.default.max-tick-time.tile", 20);
                    spigot.set("world-settings.default.max-tick-time.entity", 30);
                }
                spigot.set("world-settings.default.entity-activation-range.animals", 12);
                spigot.set("world-settings.default.entity-activation-range.monsters", 24);
                spigot.set("world-settings.default.entity-activation-range.misc", 2);
                spigot.set("world-settings.default.entity-tracking-range.other", 48);
                spigot.set("world-settings.default.random-light-updates", false);
                if (heapmb <= 4000) {
                    spigot.set("world-settings.default.save-structure-info", false);
                }
                spigot.set("world-settings.default.max-entity-collisions", 2);
                spigot.set("world-settings.default.max-tnt-per-tick", 20);
                spigot.set("EscapeLag.Changed", "如果Config的AutoSet开启，该参数会被改变。");
                spigot.save(SpigotFile);
            }
        }
        File PaperFile = new File("paper.yml");
        if (PaperFile.exists()) {
            FileConfiguration paper = AzureAPI.loadOrCreateConfiguration(PaperFile);
            File backupPaperFile = new File("backup_paper.yml");
            if (backupPaperFile.exists() == false) {
                backupPaperFile.createNewFile();
                paper.save(backupPaperFile);
                paper.set("world-settings.default.keep-spawn-loaded", false);
                paper.set("world-settings.default.optimize-explosions", true);
                paper.set("world-settings.default.fast-drain.lava", true);
                paper.set("world-settings.default.fast-drain.water", true);
                paper.set("world-settings.default.use-async-lighting", true);
                if (heapmb <= 6000) {
                    paper.set("world-settings.default.tick-next-tick-list-cap", 8000);
                }
                paper.set("world-settings.default.tick-next-tick-list-cap-ignores-redstone", true);
                paper.save(PaperFile);
            }
        }
        if (BukkitFile.exists()) {
            FileConfiguration bukkit = AzureAPI.loadOrCreateConfiguration(BukkitFile);
            if (bukkit.getInt("EscapeLag.SetStep") == 1) {
                bukkit.set("EscapeLag.SetStep", 2);
                try {
                    bukkit.save(BukkitFile);
                } catch (IOException ex) {
                }
            }
            if (bukkit.getInt("EscapeLag.SetStep") == 0) {
                bukkit.set("EscapeLag.SetStep", 1);
                bukkit.save(BukkitFile);
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, () -> {
                    AzureAPI.log("成功改动服务器配端，正在重启来启用它.");
                    AzureAPI.restartServer("配端完成，正在重启中！");
                }, 1);
            }
        }
    }
}
