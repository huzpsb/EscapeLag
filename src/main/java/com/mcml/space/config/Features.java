package com.mcml.space.config;

import java.util.Map;
import java.util.Set;

import com.mcml.space.util.Configurable;

public abstract class Features extends Configurable {
	@Node("Monitor.enable")
    public static boolean Monitorenable = true;
	
	@Node("Monitor.ThreadLagWarning")
    public static boolean MonitorThreadLagWarning = true;
	
	@Node("Monitor.PluginLagWarning.enable")
    public static boolean MonitorPluginLagWarningenable = true;
	
	@Node("Monitor.PluginLagWarning.Period")
    public static long MonitorPluginLagWarningPeriod = 1000L;
	
	@Node("PluginErrorMessageBlocker.enable")
	public static boolean PluginErrorMessageBlockerenable = true;
	
	@Node("PluginErrorMessageBlocker.Message")
	public static Set<String> PluginErrorMessageBlockerMessage = DefaultOptions.blockedErrorMessages();
	
	@Node("PluginErrorMessageLogger.enable")
	public static boolean PluginErrorMessageLoggerenable = false;
	
	@Node("AntiSpam.enable")
	public static boolean AntiSpamenable = true;

	@Node("AntiSpam.Period.Period")
	public static double AntiSpamPeriodPeriod = 1.5;

	@Locale
	@Node("AntiSpam.Period.WarnMessage")
	public static String AntiSpamPeriodWarnMessage = "§c请慢点说话，别激动嘛！ _(:з」∠)_";
	
	@Node("AntiSpam.Dirty.enable")
    public static boolean enableAntiDirty = true;

	@Node("AntiSpam.Dirty.List")
    public static Map<String, Boolean> AntiSpamDirtyList = DefaultOptions.spamMessages();
	
	@Node("AntiSpam.Dirty.white-list")
    public static Set<String> AntiSpamDirtyWhitelist = DefaultOptions.spamWhitelist();

	@Node("NoEggChangeSpawner.enable")
	public static boolean preventSpawnerModify = true;

	@Node("ProtectFarm.enable")
	public static boolean ProtectFarmenable = true;
	
	@Node("ProtectFarm.onlyPlayer")
    public static boolean ProtectFarmOnlyPlayer = false;

	@Locale
	@Node("AntiSpam.Dirty.WarnMessage")
	public static String AntiSpamDirtyWarnMessage = "§c什么事情激动得你都想骂人啦？";

	@Node("AutoRespawn.enable")
	public static boolean canAutoRespawn = true;

	@Node("AutoRespawn.RespawnTitle.enable")
	public static boolean sendTitleAutoRespawn = false;

	@Locale
	@Node("AutoRespawn.RespawnTitle.MainMessage")
	public static String titleAutoRespawn = "§e你死了！";

	@Locale
	@Node("AutoRespawn.RespawnTitle.MiniMessage")
	public static String subtitleAutoRespawn = "§c已为您自动复活！";

	@Node("NoExplode.enable")
	public static boolean controlExplode = true;

	@Node("NoExplode.Type")
	public static String explodeControlType = "NoBlockBreak";
}
