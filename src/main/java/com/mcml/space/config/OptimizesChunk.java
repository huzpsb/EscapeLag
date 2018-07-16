package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class OptimizesChunk extends Configurable {
    @Node("config-version")
    public static long configVersion = 1;
    
    // No styx chunks
    @Node("settings.no-styx-chunks.enable")
    public static boolean enableNoStyxChunks = true;
    
    @Node("settings.no-styx-chunks.chances.player-teleport")
    public static boolean noStyxChunks_chancesTeleport = true;
    
    @Node("settings.no-styx-chunks.chances.player-exit")
    public static boolean noStyxChunks_chancesExit = true;
    
    @Node("settings.no-styx-chunks.chances.time-interval.enable")
    public static boolean noStyxChunks_chancesInterval_enable = true;
    
    @Node("settings.no-styx-chunks.chances.time-interval.periods-in-mins")
    public static int noStyxChunks_chancesInterval_periods = 15;
    
    // Dealyed chunk keeper
    @Node("settings.delayed-chunk-keeper.enable")
    public static boolean enableDelayedChunkKeeper = true;
    
    @Node("settings.delayed-chunk-keeper.delay-in-seconds")
    public static int delayedChunkKeeper_delayInSeconds = 10;
    
    @Node("settings.delayed-chunk-keeper.max-unload-chunks-per-tick")
    public static int delayedChunkKeeper_maxUnloadChunksPerTick = 30;
    
    @Node("settings.delayed-chunk-keeper.post-skip-ticks")
    public static int delayedChunkKeeper_postSkipTicks = 1;
}
 