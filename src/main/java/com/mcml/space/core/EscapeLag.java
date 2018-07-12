package com.mcml.space.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;
import com.mcml.space.command.EscapeLagCommand;
import com.mcml.space.command.LocalizedHelper;
import com.mcml.space.config.ConfigFunction;
import com.mcml.space.config.ConfigMain;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.config.ConfigPatch;
import com.mcml.space.function.AntiSpam;
import com.mcml.space.function.ExplosionController;
import com.mcml.space.function.FarmProtection;
import com.mcml.space.function.RespawnAction;
import com.mcml.space.function.SpawnerController;
import com.mcml.space.function.UpgradeNotifier;
import com.mcml.space.optimize.RedstoneSlacker;
import com.mcml.space.optimize.AutoSave;
import com.mcml.space.optimize.ChunkKeeper;
import com.mcml.space.optimize.EmptyRestart;
import com.mcml.space.optimize.FireSpreadSlacker;
import com.mcml.space.optimize.UnloadClear;
import com.mcml.space.optimize.NoCrowdEntity;
import com.mcml.space.optimize.OverloadRestart;
import com.mcml.space.optimize.TickSleep;
import com.mcml.space.optimize.TeleportPreLoader;
import com.mcml.space.optimize.TimerGarbageCollect;
import com.mcml.space.optimize.WaterFlowLimitor;
import com.mcml.space.patch.AntiBedExplode;
import com.mcml.space.patch.AntiCrashSign;
import com.mcml.space.patch.AntiDupeDropItem;
import com.mcml.space.patch.AntiFakeDeath;
import com.mcml.space.patch.AntiInfItem;
import com.mcml.space.patch.AntiInfRail;
import com.mcml.space.patch.CancelledPlacementPatch;
import com.mcml.space.patch.AntiLongStringCrash;
import com.mcml.space.patch.NetherHopperDupePatch;
import com.mcml.space.patch.ContainerPortalPatch;
import com.mcml.space.patch.CalculationAbusePatch;
import com.mcml.space.patch.BonemealDupePatch;
import com.mcml.space.patch.CheatBookBlocker;
import com.mcml.space.patch.DupeLoginPatch;
import com.mcml.space.patch.NegativeItemPatch;
import com.mcml.space.patch.AutoRecipePatch;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.Coord;
import com.mcml.space.util.PlayerList;
import com.mcml.space.util.Configurable;
import com.mcml.space.util.NetWorker;
import com.mcml.space.util.Perms;
import com.mcml.space.util.Ticker;
import com.mcml.space.util.VersionLevel;

public class EscapeLag extends JavaPlugin implements Listener {
    public static EscapeLag plugin;
    public final static Map<String, Coord<FileConfiguration, Class<?>>> configurations = Maps.newHashMap(); // File name as key
    
    @Override
    public void onEnable() {
        plugin = this;
        
        setupConfigs();
        
        AzureAPI.log("EscapeLag —— 新一代的优化/稳定插件");
        AzureAPI.log("~(@^_^@)~ 玩的开心！~");
        
        AzureAPI.log("Version " + getDescription().getVersion() + " is ready for installation \n");
        AzureAPI.log("Server: " + Bukkit.getServer().getVersion());
        AzureAPI.log("Bukkit: " + Bukkit.getServer().getBukkitVersion());
        AzureAPI.log("Level: " + VersionLevel.get() + "\n");
        
        AzureAPI.log("Setup modules..");
        
        PlayerList.bind(this);
        
        if (ConfigOptimize.AutoSetenable == true) {
            try {
                EscapeLag.AutoSetServer();
            } catch (IOException | InterruptedException e) {
            }
        }
        
        PlayerList.bind(new UpgradeNotifier());
        
        Perms.bind("EscapeLag.Admin");
        
        Bukkit.getPluginManager().registerEvents(new AntiInfItem(), this);
        ContainerPortalPatch.init(this);
        NetherHopperDupePatch.init(this);
        NegativeItemPatch.init(this);
        Bukkit.getPluginManager().registerEvents(new NoCrowdEntity(), this);
        Bukkit.getPluginManager().registerEvents(new AntiCrashSign(), this);
        Bukkit.getPluginManager().registerEvents(new AntiSpam(), this);
        ExplosionController.init(this);
        RedstoneSlacker.init(plugin);
        Bukkit.getPluginManager().registerEvents(new UnloadClear(), this);
        Bukkit.getPluginManager().registerEvents(new AntiInfRail(), this);
        Bukkit.getPluginManager().registerEvents(new AutoSave(), this);
        DupeLoginPatch.init(this);
        SpawnerController.init(this);
        AntiDupeDropItem.init(this);
        Bukkit.getPluginManager().registerEvents(new CancelledPlacementPatch(), this);
        Bukkit.getPluginManager().registerEvents(new AntiBedExplode(), this);
        Bukkit.getPluginManager().registerEvents(new WaterFlowLimitor(), this);
        FireSpreadSlacker.init(this);
        FarmProtection.init(this);
        BonemealDupePatch.init(this);
        Bukkit.getPluginManager().registerEvents(new AntiLongStringCrash(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportPreLoader(), this);
        TickSleep.init(this);
        RespawnAction.init(this);
        EmptyRestart.init(this);
        CheatBookBlocker.init(this);
        OverloadRestart.init(this);
        CalculationAbusePatch.init(this);
        AutoRecipePatch.init(this);
        
        ChunkKeeper.init(this);
        
        TimerGarbageCollect.init(this);
        if (ConfigMain.AutoUpdate)
            Bukkit.getScheduler().runTaskAsynchronously(this, new NetWorker());
        Bukkit.getScheduler().runTaskTimer(this, new AntiFakeDeath(), 7 * 20, 7 * 20);
        Ticker.init();
        
        AzureAPI.log("EscapeLag has been installed successfully!");
        AzureAPI.log("乐乐感谢您的使用——有建议务必反馈，QQ1207223090");
        AzureAPI.log("您可以在插件根目录找到本插件的说明文档 说明文档.txt");
        List<String> devs = getDescription().getAuthors();
        AzureAPI.log("|||" + devs.get(0) + "/EscapeLag 合作作品.|||");
        AzureAPI.log("|||" + AzureAPI.contactBetween(devs, 1, ", ") + " 合作开发.|||");
        AzureAPI.log("§a您正在使用EscapeLag构建号 %BUILD_NUMBER%");
    }
    
    @Override
    public void onDisable() {
        AzureAPI.warn("Plugin has been disabled.");
        AzureAPI.warn("Thanks for using :)");
    }
    
    public static File getPluginFile() {
        return EscapeLag.plugin.getFile();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("el")) {
            return EscapeLagCommand.processCommand(sender, cmd, label, args);
        }
        return true;
    }
    
    private void configsFile(String file, Class<?> provider) {
        File configuration = new File(this.getDataFolder(), file);
        configurations.put(file, AzureAPI.<FileConfiguration, Class<?>>wrapCoord(AzureAPI.loadOrCreateFile(configuration), provider));
    }
    
    private void setupConfigs() {
        String locale = "english";
        if (StringUtils.startsWithIgnoreCase(ConfigMain.lang, "zh_")) locale = "中文";
        EscapeLag.plugin.saveResource("documents/" + locale + ".txt", true);
        
        configurations.clear();
        configsFile("PluginMainConfig.yml", ConfigMain.class);
        configsFile("ClearLagConfig.yml", ConfigOptimize.class);
        configsFile("AntiBugConfig.yml", ConfigPatch.class);
        configsFile("DoEventConfig.yml", ConfigFunction.class);
        
        for (Entry<String, Coord<FileConfiguration, Class<?>>> entry : configurations.entrySet()) {
            File configuration = new File(this.getDataFolder(), entry.getKey());
            try {
                Configurable.restoreNodes(AzureAPI.wrapCoord(configuration, entry.getValue().getKey()), ConfigMain.class);
            } catch (IllegalArgumentException | IllegalAccessException illegal) {
                AzureAPI.fatal("Cannot setup configuration ( " + configuration.getName() + " ), wrong format?", this);
            } catch (IOException io) {
                AzureAPI.fatal("Cannot load configuration ( " + configuration.getName() + " ), file blocked?", this);
            }
        }
        
        AzureAPI.setPrefix(ChatColor.translateAlternateColorCodes('&', ConfigMain.PluginPrefix) + ChatColor.RESET + " > ");
        LocalizedHelper.init();
    }
    
    public static void AutoSetServer() throws IOException, InterruptedException {
        long heapmb = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        File BukkitFile = new File("bukkit.yml");
        if (BukkitFile.exists()) {
            FileConfiguration bukkit = AzureAPI.loadOrCreateFile(BukkitFile);
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
            FileConfiguration spigot = AzureAPI.loadOrCreateFile(SpigotFile);
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
            FileConfiguration paper = AzureAPI.loadOrCreateFile(PaperFile);
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
            FileConfiguration bukkit = AzureAPI.loadOrCreateFile(BukkitFile);
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
                Bukkit.getScheduler().runTaskLater(EscapeLag.plugin, new Runnable() {
                    public void run() {
                        AzureAPI.log("成功改动服务器配端，正在重启来启用它.");
                        AzureAPI.restartServer("配端完成，正在重启中！");
                    }
                }, 1);
            }
        }
    }
}
