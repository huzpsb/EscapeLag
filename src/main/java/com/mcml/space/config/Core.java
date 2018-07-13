package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class Core extends Configurable {
    @Node(path = "PluginPrefix")
    public static String PluginPrefix = "&bEscapeLag";

    @Node(path = "AutoUpdate")
    public static boolean AutoUpdate = true;

    @Node(path = "internal-version")
    public static String internalVersion = String.valueOf("%BUILD_NUMBER%");

    @Node(path = "language")
    public static String lang = "zh_cn";
}
