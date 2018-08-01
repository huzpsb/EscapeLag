package com.mcml.space.config;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;
import com.mcml.space.util.Configurable;
import com.mcml.space.util.Locale;

public abstract class PatchesDupeFixes extends Configurable {
    @Node("config-version")
    public static long configVersion = 1;
    
    // Rails machine
    @Node("settings.rails-machine.enable-fixes")
    public static boolean enableRailsMachineFixes = true;
    
    // Nether hoppers
    @Node("settings.nether-hoppers.enable-fixes")
    public static boolean enableNetherHoppersDupeFixes = true;
    
    @Node("settings.nether-hoppers.worlds.restrict-environment")
    public static boolean netherHoppersDupeFixes_restrictEnv = true;
    
    @Node("settings.nether-hoppers.worlds.enable-worlds")
    public static Set<String> netherHoppersDupeFixes_worlds = Sets.newHashSet();
    
    // Negative item
    @Node("settings.negative-amount-item.enable-fixes")
    public static boolean enableNegativeItemDupeFixes = false;
    
    @Node("settings.negative-amount-item.notify-message")
    public static String negativeItemDupeFixes_notifyMesssage = Locale.isNative() ?
            "&b发现玩家 &f$player &b获取到负数物品, 已执行清理!" : "&bFound that player &f$player &bhas negative/infinite item(s), have been cleared!";
    
    @Node("settings.negative-amount-item.actions.removes-item.enable")
    public static boolean negativeItemDupeFixes_removesItem = false;
    
    @Node("settings.negative-amount-item.actions.removes-item.fliter-drops")
    public static boolean negativeItemDupeFixes_removesItem_fliterDrops = true;
    
    @Node("settings.negative-amount-item.actions.fliter-players-inv")
    public static boolean negativeItemDupeFixes_fliterPlayersInv = true;
    
    // Portal container
    @Node("settings.portal-container.enable-fixes")
    public static boolean enablePortalContainerDupeFixes = true;
    
    // Cancelled placement
    @Node("settings.cancelled-placement.enable-fixes")
    public static boolean enableCancelledPlacementDupeFixes = true;
    
    @Node("settings.cancelled-placement.clears-radius")
    public static Map<String, String> cancelledPlacementDupeFixes_clearsRadius = DefaultOptions.droppedItemClearsRadius();
}