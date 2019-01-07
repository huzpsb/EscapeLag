package com.mcml.space.config;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.Locale;
import java.util.Map;
import java.util.Set;

public abstract class Features extends Configurable {

    @Node("Monitor.enable")
    public static volatile boolean Monitorenable = false;

    @Node("Monitor.ThreadLag.Warning.enable")
    public static volatile boolean MonitorThreadLagWarning = false;
    
    @Node("Monitor.ThreadLag.Period")
    public static long MonitorThreadLagPeriod = 2000L;
    
    @Node("Monitor.ThreadLag.DumpStack")
    public static volatile boolean MonitorThreadLagDumpStack = false;

    @Node("AntiSpam.enable")
    public static boolean AntiSpamenable = false;

    @Node("AntiSpam.Period.Period")
    public static double AntiSpamPeriodPeriod = 1.5;
    
    @Node("AntiSpam.Period.Period-Command")
    public static double AntiCommandSpamPeriodPeriod = 0.5;

    @Node("AntiSpam.Period.WarnMessage")
    public static String AntiSpamPeriodWarnMessage = Locale.isNative() ? "§c请慢一点，别激动嘛！ _(:з」∠)_" : "§cBe slow, have an coffee! _(:з」∠)_";

    @Node("AntiSpam.Dirty.enable")
    public static boolean enableAntiDirty = false;

    @Node("AntiSpam.Dirty.List")
    public static Map<String, Boolean> AntiSpamDirtyList = DefaultOptions.spamMessages();

    @Node("AntiSpam.Dirty.white-list")
    public static Set<String> AntiSpamDirtyWhitelist = DefaultOptions.spamWhitelist();

    @Node("NoEggChangeSpawner.enable")
    public static boolean preventSpawnerModify = true;

    @Node("ProtectFarm.enable")
    public static boolean ProtectFarmenable = false;

    @Node("AntiSpam.Dirty.WarnMessage")
    public static String AntiSpamDirtyWarnMessage = Locale.isNative() ? "§c什么事情激动得你都想骂人啦？" : "§cWhat makes you so angry?";

    @Node("NoExplode.enable")
    public static boolean controlExplode = false;

    @Node("NoExplode.Type")
    public static String explodeControlType = "NoBlockBreak";
}
