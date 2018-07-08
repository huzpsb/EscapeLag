package com.mcml.space.config;

import java.util.List;

import com.mcml.space.util.Configurable;

public abstract class ConfigFunction extends Configurable {
	@Node(path = "Monitor.enable")
    public static boolean Monitorenable = true;
	
	@Node(path = "Monitor.ThreadLagWarning")
    public static boolean MonitorThreadLagWarning = true;
	
	@Node(path = "Monitor.PluginLagWarning.enable")
    public static boolean MonitorPluginLagWarningenable = true;
	
	@Node(path = "Monitor.PluginLagWarning.Period")
    public static long MonitorPluginLagWarningPeriod = 1000L;
	
	@Node(path = "PluginErrorMessageBlocker.enable")
	public static boolean PluginErrorMessageBlockerenable = true;
	
	@Node(path = "PluginErrorMessageBlocker.Message")
	public static List<String> PluginErrorMessageBlockerMessage = DefaultConfigurableLists.blockedErrorMessages();
	
	@Node(path = "PluginErrorMessageLogger.enable")
	public static boolean PluginErrorMessageLoggerenable = false;
	
	@Node(path = "AntiSpam.enable")
	public static boolean AntiSpamenable = true;

	@Node(path = "AntiSpam.Period.Period")
	public static double AntiSpamPeriodPeriod = 1.5;

	@Locale
	@Node(path = "AntiSpam.Period.WarnMessage")
	public static String AntiSpamPeriodWarnMessage = "§c请慢点说话，别激动嘛！ _(:з」∠)_";
	
	@Node(path = "AntiSpam.Dirty.enable")
    public static boolean enableAntiDirty = true;

	@Node(path = "AntiSpam.Dirty.List")
	public static List<String> AntiSpamDirtyList = DefaultConfigurableLists.spamMessages();

	@Node(path = "NoEggChangeSpawner.enable")
	public static boolean preventSpawnerModify = true;

	@Locale
	@Node(path = "NoEggChangeSpawner.TipMessage")
	public static String messagePreventSpawnerModify = "&c抱歉，禁止使用刷怪蛋修改刷怪笼";

	@Node(path = "ProtectFarm.enable")
	public static boolean ProtectFarmenable = true;

	@Locale
	@Node(path = "AntiSpam.Dirty.WarnMessage")
	public static String AntiSpamDirtyWarnMessage = "§c什么事情激动得你都想骂人啦？";

	@Node(path = "AutoRespawn.enable")
	public static boolean canAutoRespawn = true;

	@Node(path = "AutoRespawn.RespawnTitle.enable")
	public static boolean sendTitleAutoRespawn = false;

	@Locale
	@Node(path = "AutoRespawn.RespawnTitle.MainMessage")
	public static String titleAutoRespawn = "§e你死了！";

	@Locale
	@Node(path = "AutoRespawn.RespawnTitle.MiniMessage")
	public static String subtitleAutoRespawn = "§c已为您自动复活！";

	@Node(path = "NoExplode.enable")
	public static boolean controlExplode = true;

	@Node(path = "NoExplode.Type")
	public static String explodeControlType = "NoBlockBreak";
}
