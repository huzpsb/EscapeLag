package com.mcml.space.core;

import com.mcml.space.config.Features;
import com.mcml.space.util.AzureAPI;
import java.lang.management.ManagementFactory;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Ticker {

    private static int GameTick;
    public static int TPS;
    private static BukkitTask GameTask;
    private static Timer TPSTimer;
    private static Timer ThreadMonitorTimer;
    private static long ThreadUseTime;
    private static boolean ThreadLagWarned;
    public static int currentTick;

    public static void init() {
        AzureAPI.log("TPS系统模块已加载...");
        TPSTimer = new Timer();
        TPSTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                TPS = GameTick;
                GameTick = 0;
            }
        }, 1000, 1000);
        if (Features.Monitorenable == true && Features.MonitorThreadLagWarning == true) {
            ThreadMonitorTimer = new Timer();
            ThreadMonitorTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    ThreadUseTime++;
                    if (ThreadUseTime >= Features.MonitorThreadLagPeriod) {
                        if (ThreadLagWarned == false) {
                            if (Features.MonitorThreadLagDumpStack) {
                                Bukkit.getLogger().log(Level.WARNING, "------------------------------");
                                Bukkit.getLogger().log(Level.WARNING, "[EL侦测系统]服务器主线程已陷入停顿" + ThreadUseTime + "ms! 你的服务器卡顿了!");
                                Bukkit.getLogger().log(Level.WARNING, "当前主线程堆栈追踪 (这并非EscapeLag引起的卡顿,EL只负责报告卡顿情况):");
                                AzureAPI.dumpThread(ManagementFactory.getThreadMXBean().getThreadInfo(AzureAPI.serverThread().getId(), Integer.MAX_VALUE), Bukkit.getLogger());
                                Bukkit.getLogger().log(Level.WARNING, "------------------------------");
                            } else {
                                AzureAPI.log("服务器主线程已陷入停顿! 你的服务器卡顿了!");
                                AzureAPI.log("这并非EscapeLag引起的卡顿,EL只负责报告卡顿情况!");
                            }
                            ThreadLagWarned = true;
                        }
                    }
                }
            }, 1, 1);
        }
        GameTask = Bukkit.getScheduler().runTaskTimer(EscapeLag.plugin, new Runnable() {
            @Override
            public void run() {
                GameTick++;
                currentTick++;
                if (ThreadLagWarned) {
                    ThreadLagWarned = false;
                    Bukkit.getLogger().log(Level.WARNING, "[EL侦测系统]服务器总计停顿" + ThreadUseTime + "ms!");
                }
                ThreadUseTime = 0;
            }
        }, 1, 1);
    }

    public static void close() {
        TPSTimer.cancel();
        GameTask.cancel();
        if (Features.Monitorenable == true && Features.MonitorThreadLagWarning == true) {
            ThreadMonitorTimer.cancel();
        }
        AzureAPI.log("TPS系统模块已卸载...");
    }
}
