package com.mcml.space.command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.ConfigMain;
import com.mcml.space.core.EscapeLag;
import com.mcml.space.monitor.MonitorRecord;
import com.mcml.space.monitor.MonitorUtils;
import com.mcml.space.optimize.ChunkKeeper;
import com.mcml.space.optimize.ChunkUnloader;
import com.mcml.space.optimize.OverloadRestart;
import com.mcml.space.util.HeapDumper;
import com.mcml.space.util.NetWorker;
import com.mcml.space.util.Perms;
import com.mcml.space.util.Ticker;
import com.mcml.space.util.Utils;

public class EscapeLagCommand {
	public static boolean processCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("el")) {
			sender.sendMessage(LocalizedHelper.PluginPrefixLine);
			if (Perms.has(sender)) {
				if (args.length == 0) {
					sender.sendMessage(LocalizedHelper.PleaseEnterelToHelp);
					return true;
				}
				if (args[0].equalsIgnoreCase("updateon")) {
					FileConfiguration MainConfig = EscapeLag.configMain.getValue();
					MainConfig.set("AutoUpdate", true);
					try {
						MainConfig.save(EscapeLag.configMain.getKey());
					} catch (IOException ex) {
					}
					ConfigMain.AutoUpdate = true;
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
						Bukkit.getScheduler().runTaskAsynchronously(EscapeLag.plugin, new Runnable() {
							@Override
							public void run() {
								NetWorker.DownloadAntiAttack();
							}
						});
					}
				}
				if (args[0].equalsIgnoreCase("monitor")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§eenable 打开插件耗时侦测");
						sender.sendMessage("§edisable 关闭插件耗时侦测");
						sender.sendMessage("§etopall <显示条数> 列表插件主线程总耗时");
						sender.sendMessage("§eeventall <显示条数> 查阅所有插件监听器耗时列表");
						sender.sendMessage("§ecommandall <显示条数> 查阅所有插件命令耗时列表");
						sender.sendMessage("§etaskall <显示条数> 查阅所有插件任务耗时列表");
						sender.sendMessage("§eevent <插件名字> 查阅插件监听器耗时列表");
						sender.sendMessage("§ecmd <插件名字> 查阅插件命令耗时列表");
						sender.sendMessage("§etask <插件名字> 查阅插件任务耗时列表");
						return true;
					}
					if (args[1].equalsIgnoreCase("enable")) {
						MonitorUtils.enable();
						sender.sendMessage("§e成功注入了插件侦测器！");
					}
					if (args[1].equalsIgnoreCase("disable")) {
						MonitorUtils.disable();
						sender.sendMessage("§e成功注销了插件侦测器！");
					}
					if (args[1].equalsIgnoreCase("event")) {
						Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
						if (plugin == null) {
							sender.sendMessage("§c错误！无法检查插件需要的数据！插件不存在？");
							return true;
						}
						sender.sendMessage("§e以下是指定插件监听器耗时排序列表：");
						sender.sendMessage("§a任务名字,§c耗时总量");
						Map<String, MonitorRecord> plugineventtime = MonitorUtils.getEventTimingsByPlugin(plugin);
						Iterator<MonitorRecord> itpet = plugineventtime.values().iterator();
						while (itpet.hasNext()) {
							MonitorRecord thispet = (MonitorRecord) itpet.next();
							sender.sendMessage(
									"§b|--§a" + thispet.getName() + " , §c" + thispet.getTotalTime() / 1000000 + "秒");
						}
					}
					if (args[1].equalsIgnoreCase("command")) {
						Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
						if (plugin == null) {
							sender.sendMessage("§c错误！无法检查插件需要的数据！插件不存在？");
							return true;
						}
						sender.sendMessage("§e以下是指定插件命令耗时排序列表：");
						sender.sendMessage("§a指令,§c耗时总量");
						Map<String, MonitorRecord> plugincommandtime = MonitorUtils.getCommandTimingsByPlugin(plugin);
						Iterator<MonitorRecord> itpct = plugincommandtime.values().iterator();
						while (itpct.hasNext()) {
							MonitorRecord thispet = (MonitorRecord) itpct.next();
							sender.sendMessage(
									"§b|--§a" + thispet.getName() + " , §c" + thispet.getTotalTime() / 1000000 + "秒");
						}
					}
					if (args[1].equalsIgnoreCase("task")) {
						Plugin plugin = Bukkit.getPluginManager().getPlugin(args[2]);
						if (plugin == null) {
							sender.sendMessage("§c错误！无法检查插件需要的数据！插件不存在？");
							return true;
						}
						sender.sendMessage("§e以下是指定插件任务耗时排序列表：");
						sender.sendMessage("§a监听器名字,§c耗时总量");
						MonitorRecord plugintasktime = MonitorUtils.getTaskTimingsByPlugin(plugin);
						sender.sendMessage(
								"§b|--§a" + plugintasktime.getName() + " , §c" + plugintasktime.getTotalTime() / 1000000 + "秒");
					}
					if (args[1].equalsIgnoreCase("topall")) {
						if (args.length == 2) {
							sender.sendMessage("§e以下是插件耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugineventtime = MonitorUtils
										.getEventTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpet = plugineventtime.values().iterator();
								while (itpet.hasNext()) {
									MonitorRecord thispet = (MonitorRecord) itpet.next();
									UsedTime = UsedTime + thispet.getTotalTime();
								}
								Map<String, MonitorRecord> plugincommandtime = MonitorUtils
										.getCommandTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpct = plugincommandtime.values().iterator();
								while (itpct.hasNext()) {
									MonitorRecord thispct = (MonitorRecord) itpct.next();
									UsedTime = UsedTime + thispct.getTotalTime();
								}
								MonitorRecord plugintasktime = MonitorUtils.getTaskTimingsByPlugin(plugin);
								UsedTime = UsedTime + plugintasktime.getTotalTime();
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							if (timetop.size() < 10) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < 10; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						} else {
							sender.sendMessage("§e以下是插件耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugineventtime = MonitorUtils
										.getEventTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpet = plugineventtime.values().iterator();
								while (itpet.hasNext()) {
									MonitorRecord thispet = (MonitorRecord) itpet.next();
									UsedTime = UsedTime + thispet.getTotalTime();
								}
								Map<String, MonitorRecord> plugincommandtime = MonitorUtils
										.getCommandTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpct = plugincommandtime.values().iterator();
								while (itpct.hasNext()) {
									MonitorRecord thispct = (MonitorRecord) itpct.next();
									UsedTime = UsedTime + thispct.getTotalTime();
								}
								MonitorRecord plugintasktime = MonitorUtils.getTaskTimingsByPlugin(plugin);
								UsedTime = UsedTime + plugintasktime.getTotalTime();
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							int toShowLength = Integer.parseInt(args[2]);
							if (timetop.size() < toShowLength) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < toShowLength; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						}
					}
					if (args[1].equalsIgnoreCase("commandall")) {
						if (args.length == 2) {
							sender.sendMessage("§e以下是插件命令耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugincommandtime = MonitorUtils
										.getCommandTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpct = plugincommandtime.values().iterator();
								while (itpct.hasNext()) {
									MonitorRecord thispct = (MonitorRecord) itpct.next();
									UsedTime = UsedTime + thispct.getTotalTime();
								}
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							if (timetop.size() < 10) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < 10; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						} else {
							sender.sendMessage("§e以下是插件命令耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugincommandtime = MonitorUtils
										.getCommandTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpct = plugincommandtime.values().iterator();
								while (itpct.hasNext()) {
									MonitorRecord thispct = (MonitorRecord) itpct.next();
									UsedTime = UsedTime + thispct.getTotalTime();
								}
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							int toShowLength = Integer.parseInt(args[2]);
							if (timetop.size() < toShowLength) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < toShowLength; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						}
					}
					if (args[1].equalsIgnoreCase("taskall")) {
						if (args.length == 2) {
							sender.sendMessage("§e以下是插件任务耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								MonitorRecord plugintasktime = MonitorUtils.getTaskTimingsByPlugin(plugin);
								UsedTime = UsedTime + plugintasktime.getTotalTime();
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							if (timetop.size() < 10) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < 10; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						} else {
							sender.sendMessage("§e以下是插件任务耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								MonitorRecord plugintasktime = MonitorUtils.getTaskTimingsByPlugin(plugin);
								UsedTime = UsedTime + plugintasktime.getTotalTime();
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							int toShowLength = Integer.parseInt(args[2]);
							if (timetop.size() < toShowLength) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < toShowLength; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						}
					}
					if (args[1].equalsIgnoreCase("eventall")) {
						if (args.length == 2) {
							sender.sendMessage("§e以下是插件监听器耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugineventtime = MonitorUtils
										.getEventTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpet = plugineventtime.values().iterator();
								while (itpet.hasNext()) {
									MonitorRecord thispet = (MonitorRecord) itpet.next();
									UsedTime = UsedTime + thispet.getTotalTime();
								}
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							if (timetop.size() < 10) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < 10; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						} else {
							sender.sendMessage("§e以下是插件监听器耗时排序列表：");
							sender.sendMessage("§a插件名字,§c耗时总量");
							Map<Plugin, Long> TimeMap = new HashMap<Plugin, Long>();
							Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
							int pl = plugins.length;
							for (int i = 0; i < pl; i++) {
								Plugin plugin = plugins[i];
								long UsedTime = 0L;
								Map<String, MonitorRecord> plugineventtime = MonitorUtils
										.getEventTimingsByPlugin(plugin);
								Iterator<MonitorRecord> itpet = plugineventtime.values().iterator();
								while (itpet.hasNext()) {
									MonitorRecord thispet = (MonitorRecord) itpet.next();
									UsedTime = UsedTime + thispet.getTotalTime();
								}
								UsedTime = UsedTime/1000000;
								TimeMap.put(plugin, UsedTime);
							}
							ArrayList<Map.Entry<Plugin, Long>> timetop = Utils.sortMap(TimeMap);
							int toShowLength = Integer.parseInt(args[2]);
							if (timetop.size() < toShowLength) {
								int tts = timetop.size();
								for (int i = 0; i < tts; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							} else {
								for (int i = 0; i < toShowLength; i++) {
									sender.sendMessage("§b|--§a" + timetop.get(i).getKey().getName() + " , §c"
											+ timetop.get(i).getValue() / 1000 + "秒");
								}
							}
						}
					}
				}
				if (args[0].equalsIgnoreCase("autoset")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§eset 执行一次配端操作");
						return true;
					}
					if (args[1].equalsIgnoreCase("set")) {
						try {
							EscapeLag.AutoSetServer();
						} catch (IOException | InterruptedException e) {
						}
						sender.sendMessage("§a配端完成！重启服务器即可生效！");
					}
				}
				if (args[0].equalsIgnoreCase("chunkkeeper")) {
					if (args.length == 1) {
						sender.sendMessage("§a后置参数:");
						sender.sendMessage("§elist 查阅以及被保持的区块列表");
						sender.sendMessage("§eaddthis 将你所处区块加入保持列表");
						sender.sendMessage("§eremovethis 将你所处区块删除报错列表");
						sender.sendMessage("§eclear 清空所有区块保持列表");
						return true;
					}
					if (args[1].equalsIgnoreCase("list")) {
						sender.sendMessage("§e目前以及被保存的区块列表:" + ChunkKeeper.ShouldKeepList);
					}
					if (args[1].equalsIgnoreCase("addthis")) {
						Player p = (Player) sender;
						Chunk chunk = p.getLocation().getChunk();
						ChunkKeeper.ShouldKeepList.add(chunk);
						sender.sendMessage("§e成功将你所在区块加入保持列表");
					}
					if (args[1].equalsIgnoreCase("removethis")) {
						Player p = (Player) sender;
						Chunk chunk = p.getLocation().getChunk();
						if (ChunkKeeper.ShouldKeepList.contains(chunk)) {
							ChunkKeeper.ShouldKeepList.remove(chunk);
							sender.sendMessage("§e成功将所在区块从保持列表中删除");
						} else {
							sender.sendMessage("§e失败将所在区块从保持列表中删除，因为该区块不在保持列表中。");
						}
					}
					if (args[1].equalsIgnoreCase("clear")) {
						ChunkKeeper.ShouldKeepList.clear();
						sender.sendMessage("§e已经清空所有在保持列表的区块。");
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
						System.gc();
						sender.sendMessage("§6内存清理完毕！");
					}
					if (args[1].equalsIgnoreCase("heapshut")) {
						Bukkit.getScheduler().runTask(EscapeLag.plugin, new OverloadRestart());
						sender.sendMessage("§6成功检测一次内存濒临重启！若未发出重启提醒则意味着内存仍然充足不至于崩溃！");
					}
					if (args[1].equalsIgnoreCase("chunkunloadlog")) {
						sender.sendMessage("§a截止到目前，插件已经卸载了" + ChunkUnloader.totalUnloadedChunks + "个无用区块");
					}
					if (args[1].equalsIgnoreCase("dump")) {
						sender.sendMessage("§a开始 dump 内存堆！这可能会花费一些时间并导致服务器卡住！");
						File dumpedFile = new File(EscapeLag.plugin.getDataFolder(),"heap.hprof");
						if(dumpedFile.exists()) {
							dumpedFile.delete();
						}
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
						sender.sendMessage("§e目前服务器的TPS是 " + Ticker.getRealTimeTPS());
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					EscapeLag.trySetupConfig();
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
