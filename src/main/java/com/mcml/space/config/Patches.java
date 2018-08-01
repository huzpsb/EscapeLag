package com.mcml.space.config;

import org.bukkit.Bukkit;

import com.mcml.space.util.Configurable;
import com.mcml.space.util.Locale;
import com.mcml.space.util.VersionLevel;
import com.mcml.space.util.VersionLevel.Version;

public abstract class Patches extends Configurable {
	@Node("AntiWEcalc.WarnMessage")
    public static String AntiWEcalcWarnMessage = Locale.isNative() ? "§c禁止利用WE bug炸服! =.=" : "§cSorry but abuse //calc is forbidden! =.=";

    @Node("AntiLongStringCrash.enable")
    public static boolean AntiLongStringCrashenable = true;

    @Node("AntiLongStringCrash.WarnMessage")
    public static String AntiLongStringCrashWarnMessage = Locale.isNative() ? "§c警告！严禁利用长字符串来导致服务器崩溃！" : "§cSorry but abuse long string to trigger a crash is forbidden!";

    @Node("AntiFakeDeath.KickMessage")
    public static String messageFakedeath = "§c严禁卡假死BUG！";

    @Node("AntiFakeDeath.enable")
    public static boolean noFakedeath = true;

    @SuppressWarnings("deprecation")
    @Node("NoDoubleOnline.enable")
    public static boolean fixDupeOnline = !Bukkit.getOnlineMode() && !(VersionLevel.isHigherEquals(Version.MINECRAFT_1_7_R4) &&
            VersionLevel.isSpigot() ? Bukkit.getServer().spigot().getConfig().getBoolean("settings.bungeecord") : false); // since 1.7.10

    @Node("NoDoubleOnline.KickMessage")
    public static String messageKickDupeOnline = Locale.isNative() ? "抱歉，服务器中您已经在线了。ԅ(¯ㅂ¯ԅ)" : "Sorry but you are already online ԅ(¯ㅂ¯ԅ)";

    @Node("AntiBedExplode.enable")
    public static boolean noBedExplore = true;

    @Node("AntiBreakUseingChest.enable")
    public static boolean protectUsingChest = true;

    @Node("AntiCheatBook.enable")
    public static boolean noCheatBook = true;

    @Node("AntiCrashSign.enable")
    public static boolean fixCrashSign = true;

    @Node("AntiDupeDropItem.enable")
    public static boolean fixDupeDropItem = true;
    
    @Node("patches.vaildate-actions.enable")
    public static boolean enableVaildateActions = true;

    @Node("AntiSkullCrash.enable")
    public static boolean noSkullCrash = true;

    @Node("AntiCrashChat.enable")
    public static boolean noCrashChat = true;

    @Node("AntiCrashChat.SpecialStringWarnMessage")
    public static String AntiCrashChatSpecialStringWarnMessage = Locale.isNative() ? "§c严禁使用崩服代码炸服！" : "§cSorry but abuse special character to trigger a crash is forbidden!";

    @Node("AntiCrashChat.ColorChatWarnMessage")
    public static String AntiCrashChatColorChatWarnMessage = Locale.isNative() ? "§c抱歉！为了防止服务器被破坏，服务器禁止使用颜色代码." : "§cSorry but color code is not allowed here on account of server safety";

    @Node("AntiBreakUseingChest.WarnMessage")
    public static String AntiBreakUsingChestWarnMessage = Locale.isNative() ? "§c抱歉！您不可以破坏一个正在被使用的容器" : "§cSorry but you can't break a container that is using by others";

    @Node("AntiBedExplode.TipMessage")
    public static String AntiBedExplodeTipMessage = Locale.isNative() ? "§r你不能在这里睡觉" : "§rSorry but here is too dangerous to sleep";

    @Node("AntiCrashSign.WarnMessage")
    public static String AntiCrashSignWarnMessage = Locale.isNative() ? "§c您输入的内容太长了！" : "§rSorry but you typed too long!";

}