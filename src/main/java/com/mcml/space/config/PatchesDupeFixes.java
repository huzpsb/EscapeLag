package com.mcml.space.config;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mcml.space.util.Configurable;

public abstract class PatchesDupeFixes extends Configurable {
    @Node(path = "config-version")
    public static long configVersion = 1;
    
    // Rails machine
    @Node(path = "settings.rails-machine.enable-fixes")
    public static boolean enableRailsMachineFixes = true;
    
    // Nether hoppers
    @Node(path = "settings.nether-hoppers.enable-fixes")
    public static boolean enableNetherHoppersDupeFixes = true;
    
    @Node(path = "settings.nether-hoppers.worlds.restrict-environment")
    public static boolean netherHoppersDupeFixes_RestrictEnv = true;
    
    @Node(path = "settings.nether-hoppers.worlds.enable-worlds")
    public static Set<String> netherHoppersDupeFixes_Worlds = Sets.newHashSet();
    
    // Negative item
    @Node(path = "settings.negative-amount-item.enable-fixes")
    public static boolean enableNegativeItemDupeFixes = false;
    
    @Node(path = "settings.negative-amount-item.notify-message")
    public static String negativeItemDupeFixes_NotifyMesssage = "&b发现 &f$player &b获取到负数物品, 已执行清理!";
    
    @Node(path = "settings.negative-amount-item.removes-item.enable")
    public static boolean negativeItemDupeFixes_RemovesItem = false;
    
    @Node(path = "settings.negative-amount-item.removes-item.fliter-drops")
    public static boolean negativeItemDupeFixes_RemovesItem_FliterDrops = true;
    
    @Node(path = "settings.negative-amount-item.fliter-players-inv")
    public static boolean negativeItemDupeFixes_FliterPlayersInv = true;
    
}
