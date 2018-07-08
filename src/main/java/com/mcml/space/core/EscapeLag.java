package com.mcml.space.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.command.EscapeLagCommand;
import com.mcml.space.command.LocalizedHelper;
import com.mcml.space.config.ConfigFunction;
import com.mcml.space.config.ConfigMain;
import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.config.ConfigPatch;
import com.mcml.space.function.AntiSpam;
import com.mcml.space.function.ExplosionController;
import com.mcml.space.function.FarmProtecter;
import com.mcml.space.function.RespawnAction;
import com.mcml.space.function.SpawnerController;
import com.mcml.space.function.UpgradeNotifier;
import com.mcml.space.monitor.MonitorEnabler;
import com.mcml.space.optimize.AntiRedstone;
import com.mcml.space.optimize.AutoSave;
import com.mcml.space.optimize.ChunkKeeper;
import com.mcml.space.optimize.EmptyRestart;
import com.mcml.space.optimize.FireLimitor;
import com.mcml.space.optimize.UnloadClear;
import com.mcml.space.optimize.NoCrowdEntity;
import com.mcml.space.optimize.NoSpawnChunks;
import com.mcml.space.optimize.OverloadRestart;
import com.mcml.space.optimize.TPSSleep;
import com.mcml.space.optimize.TeleportPreLoader;
import com.mcml.space.optimize.TimerGarbageCollect;
import com.mcml.space.optimize.WaterFlowLimitor;
import com.mcml.space.patch.AntiBedExplode;
import com.mcml.space.patch.AntiCrashSign;
import com.mcml.space.patch.AntiDupeDropItem;
import com.mcml.space.patch.AntiFakeDeath;
import com.mcml.space.patch.AntiInfItem;
import com.mcml.space.patch.AntiInfRail;
import com.mcml.space.patch.AntiInfSuagr;
import com.mcml.space.patch.AntiLongStringCrash;
import com.mcml.space.patch.AntiNetherHopperInfItem;
import com.mcml.space.patch.AntiPortalInfItem;
import com.mcml.space.patch.AntiWEcalc;
import com.mcml.space.patch.BonemealDupePatch;
import com.mcml.space.patch.CheatBookBlocker;
import com.mcml.space.patch.DupeLoginPatch;
import com.mcml.space.patch.RPGItemPatch;
import com.mcml.space.patch.SkullCrashPatch;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.AzureAPI.Coord;
import com.mcml.space.util.AzurePlayerList;
import com.mcml.space.util.Configurable;
import com.mcml.space.util.NetWorker;
import com.mcml.space.util.Perms;
import com.mcml.space.util.TPSAndThread;
import com.mcml.space.util.VersionLevel;

public class EscapeLag extends JavaPlugin implements Listener {
	public static EscapeLag PluginMain;
	public static Coord<File, FileConfiguration> configOptimize;
	public static Coord<File, FileConfiguration> configPatch;
	public static Coord<File, FileConfiguration> configMain;
	public static Coord<File, FileConfiguration> configFunction;

	@Override
	public void onEnable() {
		PluginMain = this;
		AzureAPI.bind(this);
		trySetupConfig();

		AzureAPI.log("EscapeLag —— 新一代的优化/稳定插件");
		AzureAPI.log("~(@^_^@)~ 玩的开心！~");

		AzureAPI.log("Version " + getDescription().getVersion() + " is ready for installation \n");
		AzureAPI.log("Server: " + Bukkit.getServer().getVersion());
		AzureAPI.log("Bukkit: " + Bukkit.getServer().getBukkitVersion());
		AzureAPI.log("Level: " + VersionLevel.get() + "\n");

		AzureAPI.log("Setup modules..");

		AzurePlayerList.bind(this);

		if (ConfigOptimize.AutoSetenable == true) {
			try {
				EscapeLag.AutoSetServer();
			} catch (IOException | InterruptedException e) {
			}
		}

		AzurePlayerList.bind(new UpgradeNotifier());

		Perms.bind("EscapeLag.Admin");

		Bukkit.getPluginManager().registerEvents(new AntiInfItem(), this);
		Bukkit.getPluginManager().registerEvents(new AntiPortalInfItem(), this);
		Bukkit.getPluginManager().registerEvents(new AntiNetherHopperInfItem(), this);
		RPGItemPatch.init(this);
		Bukkit.getPluginManager().registerEvents(new ChunkKeeper(), this);
		Bukkit.getPluginManager().registerEvents(new NoCrowdEntity(), this);
		Bukkit.getPluginManager().registerEvents(new AntiCrashSign(), this);
		Bukkit.getPluginManager().registerEvents(new AntiSpam(), this);
		ExplosionController.init(this);
		Bukkit.getPluginManager().registerEvents(new AntiRedstone(), this);
		Bukkit.getPluginManager().registerEvents(new UnloadClear(), this);
		Bukkit.getPluginManager().registerEvents(new NoSpawnChunks(), this);
		Bukkit.getPluginManager().registerEvents(new AntiInfRail(), this);
		Bukkit.getPluginManager().registerEvents(new AutoSave(), this);
		DupeLoginPatch.init(this);
		SpawnerController.init(this);
		AntiDupeDropItem.init();
		Bukkit.getPluginManager().registerEvents(new AntiInfSuagr(), this);
		Bukkit.getPluginManager().registerEvents(new AntiBedExplode(), this);
		Bukkit.getPluginManager().registerEvents(new WaterFlowLimitor(), this);
		Bukkit.getPluginManager().registerEvents(new FireLimitor(), this);
		Bukkit.getPluginManager().registerEvents(new FarmProtecter(), this);
		BonemealDupePatch.init();
		Bukkit.getPluginManager().registerEvents(new AntiLongStringCrash(), this);
		Bukkit.getPluginManager().registerEvents(new TeleportPreLoader(), this);
		Bukkit.getPluginManager().registerEvents(new MonitorEnabler(), this);
		TPSSleep.init();
		RespawnAction.init(this);
		EmptyRestart.init(this);
		CheatBookBlocker.init(this);
		OverloadRestart.init(this);
		SkullCrashPatch.init(this);
		AntiWEcalc.init();

		ChunkKeeper.ChunkKeeperofTask();

		TimerGarbageCollect.init(this);
		if (ConfigMain.AutoUpdate)
			Bukkit.getScheduler().runTaskAsynchronously(this, new NetWorker());
		Bukkit.getScheduler().runTaskTimer(this, new AntiFakeDeath(), 7 * 20, 7 * 20);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TPSAndThread(), 1, 1);

		AzureAPI.log("EscapeLag has been installed successfully!");
		AzureAPI.log("乐乐感谢您的使用——有建议务必反馈，QQ1207223090");
		AzureAPI.log("您可以在插件根目录找到本插件的说明文档 说明文档.txt");
		List<String> devs = getDescription().getAuthors();
		AzureAPI.log("|||" + devs.get(0) + "/EscapeLag 合作作品.|||");
		AzureAPI.log("|||" + AzureAPI.contactBetween(devs, 1, ", ") + " 合作开发.|||");
		AzureAPI.log("§a您正在使用EscapeLag构建号 %BUILD_NUMBER%");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("el")) {
			return EscapeLagCommand.processCommand(sender, cmd, label, args);
		}
		return true;
	}

	public static void trySetupConfig() {
		try {
			setupConfig();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			AzureAPI.fatal("初始化配置文件时出错", EscapeLag.PluginMain);
			e.printStackTrace();
		}
	}

	private static void setupConfig() throws IllegalArgumentException, IllegalAccessException {
		EscapeLag.PluginMain.saveResource("documents/Guide-中文.txt", true);
		EscapeLag.PluginMain.saveResource("documents/Guide-english.txt", true);

		File pluginMainConfigFile = new File(EscapeLag.PluginMain.getDataFolder(), "PluginMainConfig.yml");
		configMain = AzureAPI.wrapCoord(pluginMainConfigFile, AzureAPI.loadOrCreateFile(pluginMainConfigFile));

		File clearLagConfig = new File(EscapeLag.PluginMain.getDataFolder(), "ClearLagConfig.yml");
		configOptimize = AzureAPI.wrapCoord(clearLagConfig, AzureAPI.loadOrCreateFile(clearLagConfig));

		File antiBugConfig = new File(EscapeLag.PluginMain.getDataFolder(), "AntiBugConfig.yml");
		configPatch = AzureAPI.wrapCoord(antiBugConfig, AzureAPI.loadOrCreateFile(antiBugConfig));

		File doEventConfig = new File(EscapeLag.PluginMain.getDataFolder(), "DoEventConfig.yml");
		configFunction = AzureAPI.wrapCoord(doEventConfig, AzureAPI.loadOrCreateFile(doEventConfig));

		try {
			Configurable.restoreNodes(configMain, ConfigMain.class);
		} catch (IOException io) {
			notifyFileException(pluginMainConfigFile);
		}

		try {
			Configurable.restoreNodes(configOptimize, ConfigOptimize.class);
		} catch (IOException io) {
			notifyFileException(clearLagConfig);
		}

		try {
			Configurable.restoreNodes(configPatch, ConfigPatch.class);
		} catch (IOException io) {
			notifyFileException(antiBugConfig);
		}

		try {
			Configurable.restoreNodes(configFunction, ConfigFunction.class);
		} catch (IOException io) {
			notifyFileException(doEventConfig);
		}

		AzureAPI.setPrefix(
				ChatColor.translateAlternateColorCodes('&', ConfigMain.PluginPrefix) + ChatColor.RESET + " > ");
		LocalizedHelper.init();
	}

	private static void notifyFileException(File file) {
		AzureAPI.warn("配置文件" + file.getName() + "无法正常读取或存储, 请检查文件情况");
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
				Bukkit.getScheduler().runTaskLater(EscapeLag.PluginMain, new Runnable() {
					public void run() {
						AzureAPI.log("成功改动服务器配端，正在重启来启用它.");
						AzureAPI.RestartServer("配端完成，正在重启中！");
					}
				}, 1);
			}
		}
	}

	@Override
	public void onDisable() {
		getLogger().info("EscapeLag —— 已经停止使用");
		getLogger().info("感谢您的使用——乐乐");
	}

	public static File getPluginsFile() {
		return EscapeLag.PluginMain.getFile();
	}
}
