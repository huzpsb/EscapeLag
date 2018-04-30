package com.mcml.space.config;

import java.util.List;

import com.mcml.space.util.Configurable;

public abstract class ConfigOptimize extends Configurable {	
	@Node(path = "TPSSleep.NoOneFreeze.enable")
    public static boolean TPSSleepNoOneFreezeenable = true;
	
	@Node(path = "TPSSleep.SleepMode")
    public static String TPSSleepSleepMode = "NoUse";
	
	@Node(path = "EntityClear.enable")
    public static boolean EntityClearenable = true;
	
	@Node(path = "EntityClear.CheckInterval")
    public static int EntityClearCheckInterval = 600;
	
	@Node(path = "EntityClear.LimitCount")
    public static int EntityClearLimitCount = 1200;
	
	@Node(path = "EntityClear.ClearEntityType")
    public static List<String> EntityClearClearEntityType = Default.EntityClearClearEntityType();
	
	@Node(path = "EntityClear.ClearMessage")
    public static String EntityClearClearMessage = "§a成功清除了过多的实体~~(@^_^@)~";
	
	@Node(path = "NooneRestart.enable")
	public static boolean emptyRestart = true;

	@Node(path = "NooneRestart.TimeLong")
	public static long emptyRestartDelay = 1200;
	
    @Node(path = "OverLoadMemoryRestart.enable")
    public static boolean OverLoadMemoryRestartenable = true;
    
    @Node(path = "ChunkUnloader.Interval")
    public static long ChunkUnloaderInterval = 30;
    
    @Node(path = "WaterFlowLimitor.enable")
    public static boolean WaterFlowLimitorenable = true;
    
    @Node(path = "WaterFlowLimitor.PerChunkTimes")
    public static long WaterFlowLimitorPerChunkTimes = 2;
    
    @Node(path = "AntiRedstone.enable")
    public static boolean AntiRedstoneenable = true;
    
    @Node(path = "AntiRedstone.Times")
    public static long AntiRedstoneTimes = 5;
    
    @Node(path = "FireLimitor.enable")
    public static boolean FireLimitorenable = true;
    
    @Node(path = "FireLimitor.Period")
    public static long FireLimitorPeriod = 3000L;
    
    @Node(path = "TimerGc.enable")
    public static boolean timerGC = false;
    
    @Node(path = "TeleportPreLoader.enable")
    public static boolean TeleportPreLoaderenable = true;
    
    @Locale
    @Node(path = "TimerGc.Message")
    public static String TimerGcMessage = "§e服务器清理内存中... ԅ(¯ㅂ¯ԅ)";
    
    @Node(path = "TimerGc.Period")
    public static long TimerGcPeriod = 600;

    @Node(path = "UnloadClear.DROPPED_ITEM.NoCleatDeath")
    public static boolean UnloadClearDROPPED_ITEMNoCleatDeath = true;
    
    @Node(path = "UnloadClear.DROPPED_ITEM.NoClearTeleport")
    public static boolean UnloadClearDROPPED_ITEMNoClearTeleport = false;
    
    @Node(path = "NoCrowdedEntity.enable")
    public static boolean NoCrowdedEntityenable = true;
    
    @Node(path = "NoCrowdedEntity.TypeList")
    public static List<String> NoCrowdedEntityTypeList = Default.NoCrowdedEntityTypeList();
    
    @Node(path = "NoCrowdedEntity.PerChunkLimit")
    public static int NoCrowdedEntityPerChunkLimit = 30;
    
    @Node(path = "AntiRedstone.Message")
    public static String AntiRedstoneMessage = "§c检测到高频红石在 %location% ，插件已经将其清除，不许玩了！ (╰_╯)#";
    
    @Node(path = "OverLoadMemoryRestart.HeapMBLefted")
    public static int OverLoadMemoryRestartHeapMBLefted = 130;
	
	@Node(path = "OverLoadMemoryRestart.KickMessage")
    public static String OverLoadMemoryRestartKickMessage = "抱歉！由于服务器内存过载，需要重启服务器！";
    
    @Node(path = "AntiRedstone.RemoveBlockList")
    public static List<String> AntiRedstoneRemoveBlockList = Default.AntiRedstoneRemoveBlockList();
    
    @Node(path = "AutoSave.Interval")
    public static long AutoSaveInterval = 15;
    
    @Node(path = "OverLoadMemoryRestart.WarnMessage")
    public static String OverLoadMemoryRestartWarnMessage = "服务器会在15秒后重启，请玩家不要游戏，耐心等待！ ╮(╯_╰)╭";
    
    @Node(path = "OverLoadMemoryRestart.DelayTime")
    public static int OverLoadMemoryRestartDelayTime = 15;
    
    @Node(path = "AutoSet.enable")
    public static boolean AutoSetenable = true;
    
    @Node(path = "AutoSave.enable")
    public static boolean AutoSaveenable = true;
    
    @Node(path = "UnloadClear.DROPPED_ITEM.enable")
    public static boolean UnloadClearDROPPED_ITEMenable = true;
    
    @Node(path = "ChunkKeeper.enable")
    public static boolean ChunkKeeperenable = true;
    
    @Node(path = "ChunkUnloader.enable")
    public static boolean chunkUnloader = true;
    
    @Node(path = "NoSpawnChunks.enable")
    public static boolean noSpawnChunks = true;
    
    @Node(path = "UnloadClear.enable")
    public static boolean UnloadClearenable = true;
    
    @Node(path = "UnloadClear.type")
    public static List<String> UnloadCleartype = Default.UnloadClearType();
}
