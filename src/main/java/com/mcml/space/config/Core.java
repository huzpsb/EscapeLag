package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class Core extends Configurable {
    @Node("PluginPrefix")
    public static String PluginPrefix = "&bEscapeLag";

    @Node("AutoUpdate")
    public static boolean AutoUpdate = true;

    @Node("internal-version")
    public static String internalVersion = String.valueOf("%BUILD_NUMBER%");

    @Node("language")
    public static String lang = "zh_cn";
}
