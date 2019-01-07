package com.mcml.space.config;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.Locale;
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
    public static String EntityClearClearMessage = Locale.isNative() ? "§a成功清除了过多的实体 ~~(@^_^@)~" : "§aSuccessfully cleared overflow entities ~~(@^_^@)~";

    @Node("NooneRestart.enable")
    public static boolean emptyRestart = false;

    @Node("NooneRestart.TimeLong")
    public static long emptyRestartDelay = 1200;

    @Node("OverLoadMemoryRestart.enable")
    public static boolean OverLoadMemoryRestartenable = false;

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

    @Node("TeleportPreLoader.enable")
    public static boolean TeleportPreLoaderenable = false;

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
    public static String AntiRedstoneMessage = Locale.isNative()
            ? "§c检测到高频红石在 %location% 附近，插件已经将其清除，不许玩了！ (╰_╯)#" : "§cDetected there is an overclock redstone around %location%, we just cleared it, please stop! (╰_╯)#";

    @Node("OverLoadMemoryRestart.HeapMBLefted")
    public static int OverLoadMemoryRestartHeapMBLefted = 130;

    @Node("OverLoadMemoryRestart.KickMessage")
    public static String OverLoadMemoryRestartKickMessage = Locale.isNative()
            ? "抱歉！由于服务器内存过载，需要重启服务器！" : "Sorry but we must restart due to memory overflow";

    @Node("AntiRedstone.RemoveBlockList")
    public static Set<String> AntiRedstoneRemoveBlockList = DefaultOptions.redstoneRemovalMaterialTypes();

    @Node("AutoSave.Interval")
    public static long AutoSaveInterval = 15;

    @Node("OverLoadMemoryRestart.WarnMessage")
    public static String OverLoadMemoryRestartWarnMessage = Locale.isNative()
            ? "服务器会在15秒后重启，请玩家不要游戏，耐心等待！ ╮(╯_╰)╭" : "Server will restart in 15s, please save your current work and await! ╮(╯_╰)╭";

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
