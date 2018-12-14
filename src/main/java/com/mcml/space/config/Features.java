package com.mcml.space.config;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class Features extends Configurable {

    @Node("Monitor.enable")
    public static volatile boolean Monitorenable = true;

    @Node("Monitor.ThreadLag.Warning.enable")
    public static volatile boolean MonitorThreadLagWarning = false;
    
    @Node("Monitor.ThreadLag.Period")
    public static long MonitorThreadLagPeriod = 2000L;
    
    @Node("Monitor.ThreadLag.DumpStack")
    public static volatile boolean MonitorThreadLagDumpStack = false;

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
    
    @Node("AntiSpam.Period.Period-Command")
    public static double AntiCommandSpamPeriodPeriod = 0.5;

    @Node("AntiSpam.Period.WarnMessage")
    public static String AntiSpamPeriodWarnMessage = Locale.isNative() ? "§c请慢一点，别激动嘛！ _(:з」∠)_" : "§cBe slow, have an coffee! _(:з」∠)_";

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

    @Node("AntiSpam.Dirty.WarnMessage")
    public static String AntiSpamDirtyWarnMessage = Locale.isNative() ? "§c什么事情激动得你都想骂人啦？" : "§cWhat makes you so angry?";

    @Node("AutoRespawn.enable")
    public static boolean canAutoRespawn = true;

    @Node("AutoRespawn.RespawnTitle.enable")
    public static boolean sendTitleAutoRespawn = false;

    @Node("AutoRespawn.RespawnTitle.MainMessage")
    public static String titleAutoRespawn = Locale.isNative() ? "§e你死了！" : "§eYou are died!";

    @Node("AutoRespawn.RespawnTitle.MiniMessage")
    public static String subtitleAutoRespawn = Locale.isNative() ? "§c已为您自动复活！" : "§cWe have auto-respawned for you";

    @Node("NoExplode.enable")
    public static boolean controlExplode = true;

    @Node("NoExplode.Type")
    public static String explodeControlType = "NoBlockBreak";
}
