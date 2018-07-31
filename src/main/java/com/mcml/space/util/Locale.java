package com.mcml.space.util;

import org.apache.commons.lang.StringUtils;
import com.mcml.space.config.Core;

/**
 * @author SotrForgotten
 */
public class Locale {
    private static volatile Lang language = checkLang(Core.lang);
    
    private static Lang checkLang(String i18n) {
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
        return language.equals(Lang.CHINESE_SIMPLIFIED) || language.equals(Lang.CHINESE_TRADITIONAL);
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