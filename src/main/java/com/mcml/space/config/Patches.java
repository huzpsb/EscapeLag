package com.mcml.space.config;

import org.bukkit.Bukkit;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public abstract class Patches extends Configurable {
	@Locale
	@Node(path = "AntiWEcalc.WarnMessage")
    public static String AntiWEcalcWarnMessage = "§a§l[EscapeLag]§c禁止利用WE bug炸服! =.=";

    @Node(path = "AntiLongStringCrash.enable")
    public static boolean AntiLongStringCrashenable = true;

    @Node(path = "AntiLongStringCrash.WarnMessage")
    public static String AntiLongStringCrashWarnMessage = "§c警告！严禁利用长字符串来导致服务器崩溃！";

    @Locale
    @Node(path = "AntiFakeDeath.KickMessage")
    public static String messageFakedeath = "§c严禁卡假死BUG！";

    @Node(path = "AntiFakeDeath.enable")
    public static boolean noFakedeath = true;

    @SuppressWarnings("deprecation")
    @Node(path = "NoDoubleOnline.enable")
    public static boolean fixDupeOnline = !Bukkit.getOnlineMode() && !(VersionLevel.isHigherEquals(Version.MINECRAFT_1_7_R4) &&
            VersionLevel.isSpigot() ? Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord") : false); // since 1.7.10

    @Locale
    @Node(path = "NoDoubleOnline.KickMessage")
    public static String messageKickDupeOnline = "抱歉，服务器中您已经在线了。ԅ(¯ㅂ¯ԅ)";

    @Node(path = "AntiBedExplode.enable")
    public static boolean noBedExplore = true;

    @Node(path = "AntiBreakUseingChest.enable")
    public static boolean protectUsingChest = true;

    @Node(path = "AntiCheatBook.enable")
    public static boolean noCheatBook = true;

    @Node(path = "AntiCrashSign.enable")
    public static boolean fixCrashSign = true;

    @Node(path = "AntiInfSuagr.enable")
    public static boolean AntiInfSuagrenable = true;

    @Node(path = "AntiDupeDropItem.enable")
    public static boolean fixDupeDropItem = true;

    @Node(path = "AntiInfItem.enable")
    public static boolean noInfItem = true;
    
    @Node(path = "patches.vaildate-actions.enable")
    public static boolean enableVaildateActions = false;

    @Node(path = "AntiPortalInfItem.enable")
    public static boolean fixPortalInfItem = true;

    @Node(path = "AntiSkullCrash.enable")
    public static boolean noSkullCrash = true;

    @Node(path = "AntiCrashChat.enable")
    public static boolean noCrashChat = true;

    @Locale
    @Node(path = "AntiCrashChat.SpecialStringWarnMessage")
    public static String AntiCrashChatSpecialStringWarnMessage = "§c严禁使用崩服代码炸服！";

    @Locale
    @Node(path = "AntiCrashChat.ColorChatWarnMessage")
    public static String AntiCrashChatColorChatWarnMessage = "§c抱歉！为了防止服务器被破坏，服务器禁止使用颜色代码.";

    @Locale
    @Node(path = "AntiBreakUseingChest.WarnMessage")
    public static String AntiBreakUsingChestWarnMessage = "§c抱歉！您不可以破坏一个正在被使用的容器";

    @Locale
    @Node(path = "AntiBedExplode.TipMessage")
    public static String AntiBedExplodeTipMessage = "§r你不能在这里睡觉";

    @Locale
    @Node(path = "AntiCrashSign.WarnMessage")
    public static String AntiCrashSignWarnMessage = "§c您输入的内容太长了！";

    @Locale
    @Node(path = "AntiInfItem.ClickcWarnMessage")
    public static String AntiInfItemClickcWarnMessage = "§c警告！不允许使用负数物品！";

}