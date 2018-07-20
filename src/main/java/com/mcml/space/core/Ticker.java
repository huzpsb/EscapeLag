package com.mcml.space.core;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

import javax.net.ssl.SSLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.mcml.space.util.AzureAPI;

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
     * Total ticks in last second, to diff with ticks of current second, access from different threads is forbidden
     */
    private static long cachedTotalTicks = 0;
    
    /**
     * Real-time (current) ticks, calc from the increased ticks between second and second, volatile for instant view
     */
    private static volatile int realTimeTicks = 20;
    
    /**
     * Thread instance of timer
     */
    private static Thread timerThread;
    
    /**
     * Whether the timer thread is parked, volatile for instant view
     */
    private static volatile boolean isParked;
    
    /**
     * Notify when server stucked
     */
    protected static Timer notifier;
    
    public static void init(Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // update resources from main thread
            cachedMillis = System.currentTimeMillis();
            totalTicks++;
        }, 0L, 1L);
        
        notifier = new Timer(true);
        notifier.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timerThread == null) timerThread = Thread.currentThread();
                // to check and notify main thread hang
                notifyStucked();
            }
        }, 0L, 100L);
        
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (isParked) LockSupport.unpark(timerThread);
            // to diff real-time ticks
            realTimeTicks = (int) (totalTicks - cachedTotalTicks);
            cachedTotalTicks = totalTicks;
        }, 0L, 20L);
        
        AzureAPI.log("TPS计算与监控核心模块已启用");
    }
    
    public static void notifyStucked() {
        long current = System.currentTimeMillis();
        if (current - cachedMillis >= 1000L) {
            AzureAPI.log("警告！服务器主线程陷入停顿超过1秒！这可能是有其他插件进行网络操作、出现死循环或耗时操作所致！");
            isParked = true;
            LockSupport.park();
        }
    }
    
    public enum Distance {
        MINUTE_1, MINUTES_5, MINUTES_15;
    }
    
    public static double getAverageTPS(Distance distance) {
        return Bukkit.getTPS()[distance.ordinal()];
    }
    
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
