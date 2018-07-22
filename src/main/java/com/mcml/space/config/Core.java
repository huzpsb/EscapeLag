package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class Core extends Configurable {
    @Node("PluginPrefix")
    public static String PluginPrefix = "&bEscapeLag";

    @Node("AutoUpdate")
    public static boolean AutoUpdate = false;

    @Node("language")
    public static String lang = "zh_cn";
}
