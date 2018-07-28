package com.mcml.space.config;

import com.mcml.space.util.Configurable;
import java.util.Set;

public abstract class Optimizes extends Configurable {

    @Node("TPSSleep.NoOneFreeze.enable")
    public static boolean TPSSleepNoOneFreezeenable = true;

    @Node("TPSSleep.SleepMode")
    public static String TPSSleepSleepMode = "NoUse";

    @Node("EntityClear.enable")
    public static boolean EntityClearenable = true;

    @Node("EntityClear.CheckInterval")
    public static int EntityClearCheckInterval = 600;

    @Node("EntityClear.LimitCount")
    public static int EntityClearLimitCount = 1200;

    @Node("EntityClear.ClearEntityType")
    public static Set<String> EntityClearEntityType = DefaultOptions.EntityClearEntityTypes();

    @Node("EntityClear.ClearMessage")
    public static String EntityClearClearMessage = "§a成功清除了过多的实体~~(@^_^@)~";

    @Node("NooneRestart.enable")
    public static boolean emptyRestart = true;

    @Node("NooneRestart.TimeLong")
    public static long emptyRestartDelay = 1200;

    @Node("OverLoadMemoryRestart.enable")
    public static boolean OverLoadMemoryRestartenable = true;

    @Node("WaterFlowLimitor.enable")
    public static boolean WaterFlowLimitorenable = true;

    @Node("WaterFlowLimitor.PerChunkTimes")
    public static long WaterFlowLimitorPerChunkTimes = 2;

    @Node("AntiRedstone.enable")
    public static boolean AntiRedstoneenable = true;

    @Node("AntiRedstone.drop-item")
    public static boolean dropRedstone = true;

    @Node("AntiRedstone.Times")
    public static int AntiRedstoneTimes = 5;

    @Node("FireLimitor.enable")
    public static boolean FireLimitorenable = true;

    @Node("FireLimitor.Period")
    public static long FireLimitorPeriod = 3000L;

    @Node("TimerGc.enable")
    public static boolean timerGC = false;

    @Node("TeleportPreLoader.enable")
    public static boolean TeleportPreLoaderenable = false;

    @Locale
    @Node("TimerGc.Message")
    public static String TimerGcMessage = "§e服务器清理内存中... ԅ(¯ㅂ¯ԅ)";

    @Node("TimerGc.Period")
    public static long TimerGcPeriod = 600;

    @Node("UnloadClear.DROPPED_ITEM.NoCleatDeath")
    public static boolean UnloadClearDROPPED_ITEMNoCleatDeath = true;

    @Node("UnloadClear.DROPPED_ITEM.NoClearTeleport")
    public static boolean UnloadClearDROPPED_ITEMNoClearTeleport = false;

    @Node("NoCrowdedEntity.enable")
    public static boolean NoCrowdedEntityenable = true;

    @Node("NoCrowdedEntity.TypeList")
    public static Set<String> NoCrowdedEntityTypeList = DefaultOptions.slackEntityTypes();

    @Node("NoCrowdedEntity.PerChunkLimit")
    public static int NoCrowdedEntityPerChunkLimit = 30;

    @Node("AntiRedstone.Message")
    public static String AntiRedstoneMessage = "§c检测到高频红石在 %location% 附近，插件已经将其清除，不许玩了！ (╰_╯)#";

    @Node("OverLoadMemoryRestart.HeapMBLefted")
    public static int OverLoadMemoryRestartHeapMBLefted = 130;

    @Node("OverLoadMemoryRestart.KickMessage")
    public static String OverLoadMemoryRestartKickMessage = "抱歉！由于服务器内存过载，需要重启服务器！";

    @Node("AntiRedstone.RemoveBlockList")
    public static Set<String> AntiRedstoneRemoveBlockList = DefaultOptions.redstoneRemovalMaterialTypes();

    @Node("AutoSave.Interval")
    public static long AutoSaveInterval = 15;

    @Node("OverLoadMemoryRestart.WarnMessage")
    public static String OverLoadMemoryRestartWarnMessage = "服务器会在15秒后重启，请玩家不要游戏，耐心等待！ ╮(╯_╰)╭";

    @Node("OverLoadMemoryRestart.DelayTime")
    public static int OverLoadMemoryRestartDelayTime = 15;

    @Node("AutoSet.enable")
    public static boolean AutoSetenable = true;

    @Node("AutoSave.enable")
    public static boolean AutoSaveenable = true;

    @Node("UnloadClear.DROPPED_ITEM.enable")
    public static boolean UnloadClearDROPPED_ITEMenable = true;

    @Node("UnloadClear.enable")
    public static boolean UnloadClearenable = true;

    @Node("UnloadClear.type")
    public static Set<String> UnloadCleartype = DefaultOptions.unloadClearEntityTypes();
}
