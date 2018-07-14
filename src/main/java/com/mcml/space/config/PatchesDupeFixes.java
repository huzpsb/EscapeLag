package com.mcml.space.config;

import java.util.Set;

import com.google.common.collect.Sets;
import com.mcml.space.util.Configurable;

public abstract class PatchesDupeFixes extends Configurable {
    @Node(path = "global.config-version")
    public static long configVersion = 1;
    
    // Rails machine
    @Node(path = "settings.rails-machine.enable-fixes")
    public static boolean enableRailsMachineFixes = true;
    
    // Nether hoppers
    @Node(path = "settings.nether-hoppers.enable-fixes")
    public static boolean enableNetherHoppersDupeFixes = true;
    
    @Node(path = "settings.nether-hoppers.worlds.restrict-environment")
    public static boolean netherHoppersDupeFixesRestrictEnv = true;
    
    @Node(path = "settings.nether-hoppers.worlds.enable-worlds")
    public static Set<String> netherHoppersDupeFixesWorlds = Sets.newHashSet();
}