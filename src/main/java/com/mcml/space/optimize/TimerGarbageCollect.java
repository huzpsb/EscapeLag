package com.mcml.space.optimize;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mcml.space.config.ConfigOptimize;
import com.mcml.space.util.AzureAPI;
import com.mcml.space.util.PluginExtends;

import static com.mcml.space.config.ConfigOptimize.TimerGcMessage;
import static com.mcml.space.config.ConfigOptimize.timerGC;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class TimerGarbageCollect implements PluginExtends {
    public static void init(JavaPlugin plugin) {
        if(!timerGC) return;
        
        long ticks = AzureAPI.toTicks(TimeUnit.SECONDS, ConfigOptimize.TimerGcPeriod);
        Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                long mark = System.currentTimeMillis();
                long released = collectGarbage();
                long duration = System.currentTimeMillis() - mark;
                
                if(!StringUtils.isBlank(TimerGcMessage)) {
                    String message = StringUtils.replace(TimerGcMessage, "%gc_released_memory%", String.valueOf(released) + " MB");
                    message = StringUtils.replace(message, "%gc_cost_time%", String.valueOf(duration) + " ms");
                    AzureAPI.log(message);
                }
            }
        }, ticks, ticks);
        
        AzureAPI.log("内存释放模块已启用");
    }
    
    public static long collectGarbage() {
        long before = Runtime.getRuntime().totalMemory();
        System.runFinalization();
        System.gc();
        return (before - Runtime.getRuntime().totalMemory()) / 1024 / 1024; // to mb
    }
}
