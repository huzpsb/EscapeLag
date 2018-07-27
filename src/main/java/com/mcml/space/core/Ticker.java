package com.mcml.space.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.VersionLevel.CallerSensitive;

public class Ticker {
    /**
     * The realistic time in last tick, to diff with current time, volatile for instant view
     */
    private static volatile long cachedMillis = System.currentTimeMillis();
    
    /**
     * The total ticks since this system started, volatile for instant view
     */
    private static volatile long totalTicks = 0;
    
    /**
     * Total ticks in last second, to diff with ticks of current second, access only from bukkit async timer
     */
    private static long cachedTotalTicks = 0;
    
    /**
     * Real-time (current) ticks, calc from the increased ticks between second and second, volatile for instant view
     */
    private static volatile int realTimeTicks = 20;
    
    /**
     * Thread instance of timer, volatile for instant view
     */
    private static volatile Thread timerThread;
    
    /**
     * Whether the timer thread is parked, volatile for instant view
     */
    private static volatile boolean isTimerServiceParked;
    
    /**
     * Whether we should cancel the notify task, volatile for instant view
     */
    private static volatile boolean isPendingCancelTimerService;
    
    public static void init(Plugin plugin) {
        // Tick update task (every tick)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // unpark timer thread once main thread heartbeat
            if (isTimerServiceParked) {
                LockSupport.unpark(timerThread);
                isTimerServiceParked = false;
            }
            // update resources from main thread
            cachedMillis = System.currentTimeMillis();
            totalTicks++;
        }, 0L, 1L);
        
        // Timer service (every second)
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // assagin timer thread so we can park it
                if (timerThread == null) {
                    timerThread = Thread.currentThread();
                }
                // allow cancel task on plugin disable
                if (isPendingCancelTimerService) {
                    this.cancel();
                    return;
                }
                // to check and notify main thread hang
                if (isServerThreadStucked()) {
                    AzureAPI.log("警告！服务器主线程陷入停顿超过1秒！这可能是有其他插件进行网络操作、出现死循环或耗时操作所致！");
                    // park to avoid spamming
                    isTimerServiceParked = true;
                    LockSupport.park();
                }
            }
        }, 0L, 100L);
        
        // Tick diff thread (every second)
        // Impl Note: Not merged to timer service because it's fully async, this may cause our result is not accurate
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            // to diff real-time ticks
            realTimeTicks = (int) (totalTicks - cachedTotalTicks);
            cachedTotalTicks = totalTicks;
        }, 0L, 20L);
        
        AzureAPI.log("TPS计算与监控核心模块已启用");
    }
    
    protected static void cancelTimerService() {
        isPendingCancelTimerService = true;
    }
    
    public static boolean isServerThreadStucked() {
        return System.currentTimeMillis() - cachedMillis >= 1000L;
    }
    
    public enum Distance {
        MINUTE_1, MINUTES_5, MINUTES_15;
    }
    
    @CallerSensitive(server = "paper")
    public static double getAverageTPS(Distance distance) {
        return Bukkit.getTPS()[distance.ordinal()];
    }
    
    @CallerSensitive(server = "paper")
    public static double getRecentTPS(){
        return Bukkit.getTPS()[0];
    }
    
    public static int getRealTimeTPS(){
        return realTimeTicks;
    }
    
    public static long currentTick(){
        return totalTicks;
    }
}
