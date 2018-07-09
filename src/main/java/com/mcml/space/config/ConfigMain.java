package com.mcml.space.config;

import com.mcml.space.util.Configurable;

public abstract class ConfigMain extends Configurable {
    @Node(path = "PluginPrefix")
    public static String PluginPrefix = "&bEscapeLag";

    @Node(path = "AutoUpdate")
    public static boolean AutoUpdate = true;

    @Node(path = "internal-version")
    public static String internalVersion = String.valueOf("build-%BUILD_NUMBER%"); // Add prefix to ensure string type

    @Node(path = "language")
    public static String lang = "zh_cn";
}
