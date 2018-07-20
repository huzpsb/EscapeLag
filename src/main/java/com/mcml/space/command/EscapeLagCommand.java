package com.mcml.space.command;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.mcml.space.config.Core;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.core.Network;
import com.mcml.space.core.Ticker;
import com.mcml.space.core.Ticker.Distance;
import com.mcml.space.optimizations.DelayedChunkKeeper;
import com.mcml.space.optimizations.NoStyxChunks;
import com.mcml.space.optimizations.OverloadRestart;
import com.mcml.space.optimizations.TimerGarbageCollect;
import com.mcml.space.util.HeapDumper;
import com.mcml.space.util.Perms;
import com.mcml.space.util.VersionLevel;

public class EscapeLagCommand {
    private final static DateFormat timestamp_format = new SimpleDateFormat("yyyyMMddHHmmss"); 
    private final static DecimalFormat tps_format = new DecimalFormat("#.00");
    
	public static boolean processCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("el")) {
			sender.sendMessage(LocalizedHelper.PluginPrefixLine);
			if (Perms.has(sender)) {
				if (args.length == 0) {
					sender.sendMessage(LocalizedHelper.PleaseEnterelToHelp);
					return true;
				}
				if (args[0].equalsIgnoreCase("updateon")) {
					FileConfiguration MainConfig = EscapeLag.configurations.get(EscapeLag.CONFIG_CORE).getValue();
					MainConfig.set("AutoUpdate", true);
					try {
						MainConfig.save(EscapeLag.configurations.get(EscapeLag.CONFIG_CORE).getKey());
					} catch (IOException ex) {
					}
					Core.AutoUpdate = true;
					sender.sendMessage(LocalizedHelper.Sucesstoopenupdate);
				}
				if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage(LocalizedHelper.HelpLine1);
					sender.sendMessage(LocalizedHelper.HelpLine2);
					sender.sendMessage(LocalizedHelper.HelpLine3);
					sender.sendMessage(LocalizedHelper.HelpLine4);
					sender.sendMessage(LocalizedHelper.HelpLine5);
					sender.sendMessage(LocalizedHelper.HelpLine6);
					sender.sendMessage(LocalizedHelper.HelpLine7);
					sender.sendMessage(LocalizedHelper.HelpLine8);
				}
				if (args[0].equalsIgnoreCase("antiattack")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§edownload 下载反压测模块");
						return true;
					}
					if (args[1].equalsIgnoreCase("download")) {
						sender.sendMessage("§e操作开始执行中...");
						Bukkit.getScheduler().runTaskAsynchronously(EscapeLag.plugin, Network::DownloadAntiAttack);
					}
				}
				
				if (args[0].equalsIgnoreCase("autoset")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§eset 执行一次配端操作");
						return true;
					}
					if (args[1].equalsIgnoreCase("set")) {
					    EscapeLag.AutoSetServer(true);
						sender.sendMessage("§a配端完成！重启服务器即可生效！");
					}
				}
				if (args[0].equalsIgnoreCase("chunkkeeper")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§elist 查阅以及被保持的区块列表");
						return true;
					}
					if (args[1].equalsIgnoreCase("list")) {
						sender.sendMessage("§e目前正在延时卸载的区块列表:" + (VersionLevel.isPaper() ? "未启用" : DelayedChunkKeeper.DEALYED_CHUNKS));
					}
				}
				if (args[0].equalsIgnoreCase("heap")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§elook 查阅内存使用情况");
						sender.sendMessage("§eclearheap 强制用java回收内存");
						sender.sendMessage("§eclearchunk 执行一次检测清理区块");
						sender.sendMessage("§eheapshut 执行一次濒临崩溃内存检测");
						sender.sendMessage("§echunkunloadlog 查阅区块卸载计数器");
						sender.sendMessage("§edump 将服务器当前内存堆化为.hprof文件");
						return true;
					}
					if (args[1].equalsIgnoreCase("look")) {
						sender.sendMessage("§e最大内存 §a" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
						sender.sendMessage("§e剩余内存 §b" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB");
						sender.sendMessage("§e分配内存 §c" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB");
					}
					if (args[1].equalsIgnoreCase("clearheap")) {
						TimerGarbageCollect.collectGarbage();
						sender.sendMessage("§6内存清理完毕！");
					}
					if (args[1].equalsIgnoreCase("heapshut")) {
						Bukkit.getScheduler().runTask(EscapeLag.plugin, new OverloadRestart());
						sender.sendMessage("§6成功检测一次内存濒临重启！若未发出重启提醒则意味着内存仍然充足不至于崩溃！");
					}
					if (args[1].equalsIgnoreCase("chunkunloadlog")) {
						sender.sendMessage("§a截止到目前，插件已经卸载了" + NoStyxChunks.totalUnloadedChunks + "个无用区块");
					}
					if (args[1].equalsIgnoreCase("dump")) {
						sender.sendMessage("§a开始 dump 内存堆！这可能会花费一些时间并导致服务器卡住！");
						File rootDir = EscapeLag.getPluginFile().getParentFile().getParentFile();
						File dumpedFile = new File(rootDir, "heap_" + timestamp_format.format(System.currentTimeMillis()) + ".hprof");
						if (dumpedFile.exists()) dumpedFile.delete();
						HeapDumper.dumpHeap(dumpedFile);
						sender.sendMessage("§adump已经完成，储存为: " + dumpedFile);
					}
				}
				if (args[0].equalsIgnoreCase("autosave")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§esavethis 将自己所在区块存储");
						return true;
					}
					if (args[1].equalsIgnoreCase("savethis")) {
						Player p = (Player) sender;
						p.getLocation().getChunk().unload(true);
						p.getLocation().getChunk().load();
						sender.sendMessage("§e已经尝试储存区块，该操作不安全!");
					}
				}
				if (args[0].equalsIgnoreCase("tps")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§esleep <ms> 停顿主线程毫秒");
						sender.sendMessage("§etps 获取服务器当前TPS");
						return true;
					}
					if (args[1].equalsIgnoreCase("sleep")) {
						sender.sendMessage("§e成功强制停顿了线程" + args[2] + "毫秒");
						try {
							Thread.sleep(Long.parseLong(args[2]));
						} catch (Exception ex) {
							sender.sendMessage("§c警告，出现错误!" + ex.toString());
						}
					}
					if (args[1].equalsIgnoreCase("tps")) {
						sender.sendMessage("§e一秒钟内的实时TPS: " + Ticker.getRealTimeTPS());
						if (VersionLevel.isPaper()) {
		                      sender.sendMessage("§e一分钟内的平均TPS: " + tps_format.format(Ticker.getRecentTPS()));
		                      sender.sendMessage("§e五分钟内的平均TPS: " + tps_format.format(Ticker.getAverageTPS(Distance.MINUTES_5)));
		                      sender.sendMessage("§e一刻钟内的平均TPS: " + tps_format.format(Ticker.getAverageTPS(Distance.MINUTES_15)));
						}
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					EscapeLag.plugin.setupConfigs();
					EscapeLag.plugin.clearModules();
					EscapeLag.plugin.bindCoreModules();
					sender.sendMessage("§a§l[EscapeLag]配置已经成功重载！");
					return true;
				}
			} else {
				sender.sendMessage("§a§l[EscapeLag]§4抱歉！您没有足够的权限！");
			}
			return true;
		}
		return false;
	}
}
