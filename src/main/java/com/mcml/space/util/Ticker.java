package com.mcml.space.util;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

import com.mcml.space.config.ConfigFunction;
import com.mcml.space.core.EscapeLag;

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
    
    public static void init() {
        Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable() {
            @Override
            public void run() {
                // update resources from main thread
                cachedMillis = System.currentTimeMillis();
                totalTicks++;
            }
        }, 0L, 1L);
        
        if (ConfigFunction.Monitorenable && ConfigFunction.MonitorThreadLagWarning)
        Bukkit.getScheduler().runTaskTimerAsynchronously(EscapeLag.plugin, new Runnable() {
            @Override
            public void run(){
                // to check and notify main thread hang
                notifyStucked();
            }
        }, 0L, 1L);
        
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // to diff real-time ticks
                realTimeTicks = (int) (totalTicks - cachedTotalTicks);
                cachedTotalTicks = totalTicks;
            }
        }, 0L, 1000L);
    }
    
    public static boolean notifyStucked() {
        long current = System.currentTimeMillis();
        if (current - cachedMillis >= 1000L) { // TODO configurable
            AzureAPI.log("警告！服务器主线程陷入停顿超过1秒！这可能是有其他插件进行网络操作、出现死循环或耗时操作所致！");
            return true;
        }
        return false;
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
    
    public static double getRealTimeTPS(){
        return realTimeTicks;
    }
}
