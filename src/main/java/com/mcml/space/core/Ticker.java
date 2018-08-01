package com.mcml.space.core;

import com.mcml.space.config.Features;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.Locale;
import com.mcml.space.util.VersionLevel.CallerSensitive;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import javax.annotation.concurrent.ThreadSafe;

import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@ThreadSafe
public class Ticker {

    /**
     * The realistic time in last tick, to diff with current time, volatile for instant view
     */
    private static volatile long cachedMillis = System.currentTimeMillis();

    /**
     * The total ticks since this system started, volatile for instant view
     */
    private static volatile int totalTicks;

    /**
     * Total ticks in last second, to diff with ticks of current second, access only from bukkit async timer
     */
    private static int cachedTotalTicks = 0;

    /**
     * Real-time (current) ticks, calc from the increased ticks between second and second, volatile for instant view
     */
    private static volatile int realTimeTicks = 20;

    /**
     * Whether the timer thread is parked, atomic for modify across threads
     */
    private static final AtomicBoolean isTimerServiceParked = new AtomicBoolean();

    /**
     * Whether we should cancel the notify task, volatile for instant view
     */
    @Setter public static volatile boolean isPendingCancelTimerService;

    /**
     * Whether we should notify server owner when stucked
     */
    @Setter
    private static volatile boolean notifyConsoleOnStucked = true;

    public static void init(Plugin plugin) {
        // Reset cancellation on reload to avoid using AtomicBoolean because of low performance
        isPendingCancelTimerService = false;
     // Reset cancellation on reload to avoid unaccurate tps
        totalTicks = 0;
        
        // Tick update task (every tick)
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            // unpark timer thread once main thread heartbeat
            if (Features.Monitorenable && Features.MonitorThreadLagWarning && // Configurable
                    isTimerServiceParked.get()) { // Respect park
                isTimerServiceParked.getAndSet(false);
            }
            
            // update resources from main thread
            cachedMillis = System.currentTimeMillis();
            totalTicks++;
        }, 0L, 1L);
        
        // Timer service (every 1/10s)
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // allow cancel task on plugin disable
                if (isPendingCancelTimerService) {
                    this.cancel();
                    return;
                }
                
                // to check and notify main thread hang
                if (notifyConsoleOnStucked && // Respect our sleep command
                        Features.Monitorenable && Features.MonitorThreadLagWarning && // Configurable
                        !isTimerServiceParked.get() && // Respect park
                        isServerThreadStucked()) { // Actually stucked?
                    // print current stacks of main thread
                    if (Features.MonitorThreadLagDumpStack) {
                        Bukkit.getLogger().log(Level.WARNING, "------------------------------");
                        Bukkit.getLogger().log(Level.WARNING, Locale.isNative() ? "Your server was stucked cause main thread is blocked!" : "服务器主线程已陷入停顿! 你的服务器卡顿了!");
                        Bukkit.getLogger().log(Level.WARNING, Locale.isNative() ? "Stack Trace (The lagg was not cause by EscapeLag, we just let you know it!):" : "当前主线程堆栈追踪 (这并非EscapeLag引起的卡顿,EL只负责报告卡顿情况):");
                        AzureAPI.dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(AzureAPI.serverThread().getId(), Integer.MAX_VALUE), Bukkit.getLogger());
                        Bukkit.getLogger().log(Level.WARNING, "------------------------------");
                    } else {
                        AzureAPI.log(Locale.isNative() ? "Your server was stucked cause main thread is blocked!" : "服务器主线程已陷入停顿! 你的服务器卡顿了!");
                        AzureAPI.log(Locale.isNative() ? "The lagg was not cause by EscapeLag, we just let you know it!" : "这并非 EscapeLag 引起的卡顿, EscapeLag 只负责报告卡顿情况!");
                    }
                    
                    // park to avoid spamming
                    isTimerServiceParked.getAndSet(true);
                }
            }
        }, 0L, 100L);
        
        // Timer service (every second)
        new Timer(true).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // allow cancel task on plugin disable
                if (isPendingCancelTimerService) {
                    this.cancel();
                    return;
                }
                
                // to diff real-time ticks
                // Impl Note: bukkit async timer is not fully async, this will cause our result is not accurate when server stucked
                int realTimeHeartbeats = totalTicks - cachedTotalTicks;
                realTimeTicks = realTimeHeartbeats > 20 ? 20 : realTimeHeartbeats;
                cachedTotalTicks = totalTicks;
            }
        }, 0L, 1000L);
        
        AzureAPI.log(Locale.isNative() ? "核心模块 - 效能计算 已启用" : "Coremodule - Ticker has been enabled");
    }

    public static boolean isServerThreadStucked() {
        return System.currentTimeMillis() - cachedMillis >= Features.MonitorThreadLagPeriod; // TODO configurable
    }

    public enum Distance {
        MINUTE_1, MINUTES_5, MINUTES_15;
    }

    @CallerSensitive(server = "paper")
    public static double getAverageTPS(Distance distance) {
        return Bukkit.getTPS()[distance.ordinal()];
    }

    @CallerSensitive(server = "paper")
    public static double getRecentTPS() {
        return Bukkit.getTPS()[0];
    }

    public static int getRealTimeTPS() {
        return realTimeTicks;
    }

    public static int currentTick() {
        return totalTicks;
    }
}
