package com.mcml.space.util;

import com.mcml.space.config.Core;
import org.apache.commons.lang.StringUtils;

/**
 * @author SotrForgotten
 */
public class Locale {
    private static volatile Lang language = checkLang(Core.lang);
    
    public static Lang checkLang(String i18n) {
        if (i18n.equalsIgnoreCase("zh_cn") || i18n.equalsIgnoreCase("zh_sg")) {
            return Lang.CHINESE_SIMPLIFIED;
        }
        
        if (StringUtils.startsWithIgnoreCase(i18n, "zh_")) {
            return Lang.CHINESE_TRADITIONAL;
        }
        
        if (StringUtils.startsWithIgnoreCase(i18n, "en_")) {
            return Lang.ENGLISH;
        }
        
        AzureAPI.warn("Cannot capture language, set as global.");
        return Lang.GLOBAL;
    }
    
    public static boolean isNative() {
        return language == Lang.CHINESE_SIMPLIFIED || language == Lang.CHINESE_TRADITIONAL;
    }
    
    public static boolean equals(Lang other) {
        return language == other;
    }
    
    public enum Lang {
        GLOBAL,
        
        ENGLISH,
        CHINESE_SIMPLIFIED,
        CHINESE_TRADITIONAL
    }
}