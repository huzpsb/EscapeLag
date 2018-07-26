package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class Core extends Configurable {
    @Node("PluginPrefix")
    public static String PluginPrefix = "&bEscapeLag";
    
    @View
    @Node("internal-version")
    public static String internalVersion = String.valueOf("%BUILD_NUMBER%");

    @Node("AutoUpdate")
    public static boolean AutoUpdate = true;

    @Node("language")
    public static String lang = "zh_cn";
    
    @Node("hook-restart-by-script")
    public static boolean hookRestartByScript = false;
}
